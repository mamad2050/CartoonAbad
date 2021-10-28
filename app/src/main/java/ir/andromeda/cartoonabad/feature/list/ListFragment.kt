package ir.andromeda.cartoonabad.feature.list

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.Service
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.common.NOTIFICATION_CHANNEL_ID
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.downloaded.Downloaded
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.databinding.FragmentListBinding
import ir.andromeda.cartoonabad.feature.main.DrawerLocker
import ir.andromeda.cartoonabad.feature.player.PlayerActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.io.File

class ListFragment : CartoonAbadFragment(), EpisodeEventListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var adapter: SeasonAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: ListViewModel by viewModel { parametersOf(args.animation.id) }
    private val dirPath =
        Environment.DIRECTORY_DOWNLOADS + File.separator + "CartoonAbad" + File.separator

    var readPermissionGranted = false
    var writePermissionGranted = false
    lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

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

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(cartoonAbadEvent: CartoonAbadEvent) {
        when (cartoonAbadEvent.type) {
            CartoonAbadEvent.Type.SIMPLE -> snackBar(
                cartoonAbadEvent.stringMessage
                    ?: getString(cartoonAbadEvent.resMessage)
            )

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

        val downloadManager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(episode.url)

        val request = DownloadManager.Request(downloadUri).apply {

            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setAllowedOverRoaming(false)
            setTitle(episode.name)
            setDescription("CartoonAbad")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalFilesDir(requireContext(),
                Environment.DIRECTORY_DOWNLOADS,"CartoonABad" + File.separator + episode.url.substringAfterLast('/')
            )

        }

        val downloadId = downloadManager.enqueue(request)

        val query = DownloadManager.Query().setFilterById(downloadId)

        lifecycleScope.launchWhenStarted {

            var isDownloading = true
            while (isDownloading) {
                val cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    isDownloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                downloadStatus(episode, status)
                cursor.close()
            }
        }
    }


    private fun downloadStatus(episode: Episode, status: Int) {

        when (status) {
            DownloadManager.STATUS_FAILED -> Toast.makeText(
                requireContext(),
                "Failed",
                Toast.LENGTH_SHORT
            ).show()
            DownloadManager.STATUS_PAUSED -> Toast.makeText(
                requireContext(),
                "Paused",
                Toast.LENGTH_SHORT
            ).show()
            DownloadManager.STATUS_SUCCESSFUL -> {

                val downloaded = Downloaded(
                    episode.duration,
                    episode.id,
                    episode.image,
                    episode.name,
                    episode.season_id,
                    episode.url.substringAfterLast('/')
                )
                viewModel.addEpisodeToDownloads(downloaded)

                Toast.makeText(requireContext(), "Completed", Toast.LENGTH_SHORT).show()

            }
            else -> Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
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

        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk29


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

}
