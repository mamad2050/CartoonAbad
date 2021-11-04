package ir.andromeda.cartoonabad.feature.list

import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.common.ZONE_ID_INTERSTITIAL_VIDEO_AD
import ir.andromeda.cartoonabad.common.ZONE_ID_REWARD_AD
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.PurchaseContainer
import ir.andromeda.cartoonabad.data.downloaded.Downloaded
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.databinding.FragmentListBinding
import ir.andromeda.cartoonabad.feature.main.DrawerLocker
import ir.andromeda.cartoonabad.feature.player.PlayerActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

var isDownload = false

class ListFragment : CartoonAbadFragment(), EpisodeEventListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var adapter: SeasonAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: ListViewModel by viewModel { parametersOf(args.animation.id) }
    var readPermissionGranted = false
    private val isAllowToBackPress = true
    var writePermissionGranted = false
    private lateinit var downloadManager: DownloadManager
    lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var adResponseId: String? = null
    private val args: ListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        downloadManager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(cartoonAbadEvent: CartoonAbadEvent) {
        when (cartoonAbadEvent.type) {
            CartoonAbadEvent.Type.SIMPLE -> {
                val connectionView = showConnectionLost(true)
                connectionView?.findViewById<MaterialButton>(R.id.btnRetry)?.setOnClickListener {
                    showConnectionLost(false)
                    viewModel.showSeasons()
                }
            }

            CartoonAbadEvent.Type.PURCHASE -> {
                findNavController().navigate(R.id.navigateToPurchaseAlertDialog)
            }
        }
    }

    private fun snackBar(message: String) {
        Snackbar.make(
            activity?.findViewById(R.id.contentRootView) as View, message, Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as DrawerLocker).setDrawerLocked(true)

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        viewModel.seasonsLiveData.observe(viewLifecycleOwner) {

            viewModel.downloadsLiveData.observe(viewLifecycleOwner) { downloads ->
                downloads.forEach { downloaded ->
                    val file = File(downloaded.path)
                    it.forEach { seasons ->
                        seasons.episodeList.forEach { episode ->
                            if (episode.id == downloaded.id && file.exists()) {
                                episode.isDownloaded = true
                            }
                        }
                    }
                }
            }
            binding.rvSeasons.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = SeasonAdapter(it, imageLoadingService, requireContext(), this)
            binding.rvSeasons.adapter = adapter

        }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
                readPermissionGranted =
                    permission[android.Manifest.permission.READ_EXTERNAL_STORAGE]
                        ?: readPermissionGranted
                writePermissionGranted =
                    permission[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]
                        ?: writePermissionGranted
            }
    }

    override fun onStop() {
        super.onStop()
        _binding = null
        (activity as DrawerLocker).setDrawerLocked(false)
        EventBus.getDefault().unregister(this)
    }

    override fun onEpisodeClick(episode: Episode) {

        startActivity(Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, episode)

        })
        if (PurchaseContainer.purchaseInfo == null) {
            requestAd()
        }
    }

    override fun onFavoriteClick(episode: Episode) {
        viewModel.addEpisodeToFavorites(episode)
    }

    override fun onDownloadClick(episode: Episode) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (requireActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                startDownloading(episode)
            } else {
                requestPermissions()
            }
        } else {
            startDownloading(episode)
        }
    }

    private fun startDownloading(episode: Episode) {


        val filePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/CartoonAbad/" + episode.url.substringAfterLast('/')

        if (File(filePath).exists()) {
            snackBar(getString(R.string.already_downloaded))
        } else if (!isDownload) {

            val downloadUri = Uri.parse(episode.url)
            val request = DownloadManager.Request(downloadUri).apply {

                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                setAllowedOverRoaming(false)
                setTitle(episode.name)
                setDescription("CartoonAbad")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "CartoonAbad" + File.separator + episode.url.substringAfterLast('/')
                )
            }

            val downloadId = downloadManager.enqueue(request)

            CoroutineScope(Dispatchers.IO).launch {

                val query = DownloadManager.Query().setFilterById(downloadId)
                isDownload = true

                while (isDownload) {

                    val cursor = downloadManager.query(query)
                    if (cursor != null && cursor.moveToFirst()) {
                        val status =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        downloadStatus(episode, status)

                        cursor.close()
                    }
                }
            }

        } else {
            snackBar(getString(R.string.download_status))
        }


    }

    private fun downloadStatus(episode: Episode, status: Int) {

        when (status) {

            DownloadManager.STATUS_FAILED -> isDownload = false

            DownloadManager.STATUS_PAUSED -> {
            }

            DownloadManager.STATUS_PENDING -> {
            }

            DownloadManager.STATUS_RUNNING -> {
            }


            DownloadManager.STATUS_SUCCESSFUL -> {

                isDownload = false

                val path =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString() + "/CartoonAbad/" + episode.url.substringAfterLast('/')

                val downloaded = Downloaded(
                    episode.duration,
                    episode.id,
                    episode.image,
                    episode.name,
                    episode.season_id,
                    path
                )
                viewModel.addEpisodeToDownloads(downloaded)

            }
            else -> isDownload = false

        }

    }

    private fun requestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val hasWritePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission

        val permissionToRequest = mutableListOf<String>()

        if (!writePermissionGranted) {
            permissionToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!readPermissionGranted) {
            permissionToRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionToRequest.toTypedArray())
        }

    }

    private fun requestAd() {

        TapsellPlus.requestRewardedVideoAd(
            requireActivity(),
            ZONE_ID_REWARD_AD,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    adResponseId = tapsellPlusAdModel.responseId
                    showAd()
                }

                override fun error(message: String?) {}
            })
    }

    private fun showAd() {

        TapsellPlus.showRewardedVideoAd(requireActivity(), adResponseId,
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                }

                override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onClosed(tapsellPlusAdModel)
                }

                override fun onRewarded(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onRewarded(tapsellPlusAdModel)

                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    super.onError(tapsellPlusErrorModel)
                }
            })
    }

}



