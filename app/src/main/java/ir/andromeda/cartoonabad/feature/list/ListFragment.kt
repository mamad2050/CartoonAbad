package ir.andromeda.cartoonabad.feature.list

import android.app.ProgressDialog
import android.app.Service
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.helper.widget.MotionEffect.AUTO
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.downloader.PRDownloader
import com.google.android.material.snackbar.Snackbar
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.databinding.FragmentListBinding
import ir.andromeda.cartoonabad.feature.main.DrawerLocker
import ir.andromeda.cartoonabad.feature.player.PlayerActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ListFragment : CartoonAbadFragment(), EpisodeEventListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var adapter: SeasonAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: ListViewModel by viewModel { parametersOf(args.animation.id) }
    private lateinit var serviceIntent: Intent
    var readPermissionGranted = false
    var writePermissionGranted = false
    lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var downloadService: DownloadService
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

        serviceIntent = Intent(activity, DownloadService::class.java)
        activity?.bindService(serviceIntent, serviceConnection, Service.BIND_AUTO_CREATE)

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

//        var downloadId = 0
//        val dialog = ProgressDialog(requireContext())
//        dialog.setTitle("Downloading")
//        dialog.setMessage("Preparing")
//        dialog.setCancelable(false)
//        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
//        dialog.setButton(
//            DialogInterface.BUTTON_NEGATIVE,
//            "Cancel"
//        ) { dialog, _ ->
//
//            dialog.dismiss()
//            PRDownloader.cancel(downloadId)
//        }
//
//        val config = PRDownloaderConfig.newBuilder()
//            .setDatabaseEnabled(true)
//            .build()
//        PRDownloader.initialize(requireContext(), config)

//        val dirPath =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath+
//                    File.separator + "CartoonAbad" + File.separator
//        val fileName = episode.url.substringAfterLast('/')

//        downloadId = PRDownloader.download(episode.url, dirPath, fileName)
//            .build()
//            .setOnStartOrResumeListener {
//                dialog.setTitle("Started")
//                dialog.show()
//            }
//            .setOnProgressListener { progress ->
//                val progressPercent = progress.currentBytes * 100 / progress.totalBytes
//                dialog.progress = progressPercent.toInt()
//                dialog.setMessage(toMB(progress.currentBytes) + "/" + toMB(progress.totalBytes))
//
//            }
//            .setOnCancelListener {
//                Toast.makeText(requireContext(), "Download Canceled", Toast.LENGTH_SHORT).show()
//            }
//            .setOnPauseListener {
//
//            }
//            .start(object : OnDownloadListener {
//                override fun onDownloadComplete() {
//
//                    Toast.makeText(requireContext(), "Download Complete", Toast.LENGTH_SHORT).show()
//                    dialog.dismiss()
//                }
//
//                override fun onError(error: Error?) {
//                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
//
//                }
//
//            })

        val downloadService = DownloadService()
        downloadService.startDownload(episode)

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

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as DownloadService.EpisodeBinder
            downloadService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

}
