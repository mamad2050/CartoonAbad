package ir.andromeda.cartoonabad.feature.detail

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadActivity
import ir.andromeda.cartoonabad.common.EXTRA_KEY_NAME
import ir.andromeda.cartoonabad.common.EXTRA_KEY_URL
import ir.andromeda.cartoonabad.common.ZONE_ID_REWARD_AD
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.PurchaseContainer
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.databinding.ActivityDetailCartoonBinding
import ir.andromeda.cartoonabad.feature.player.PlayerActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailCartoonActivity : CartoonAbadActivity() {

    private lateinit var binding: ActivityDetailCartoonBinding
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: DetailCartoonViewModel by viewModel { parametersOf(intent.extras) }

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private lateinit var downloadManager: DownloadManager
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var adResponseId: String? = null

    private lateinit var cartoon: Cartoon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCartoonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        viewModel.cartoonLiveData.observe(this) { cartoon ->
            binding.tvName.text = cartoon.name
            binding.tvRate.text = "امتیاز ${cartoon.rate} / 10"
            binding.tvDescription.text = cartoon.description
            imageLoadingService.load(binding.ivImage, cartoon.image)
            this.cartoon = cartoon
        }

        binding.btnPlayCartoon.setOnClickListener {
            if (PurchaseContainer.purchaseInfo == null) {
                requestVideoAd(cartoon)
            } else {
                startActivity(Intent(this, PlayerActivity::class.java).apply {
                    putExtra(EXTRA_KEY_NAME, cartoon.name)
                    putExtra(EXTRA_KEY_URL, cartoon.url)
                })
            }
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

    override fun onStart() {
        super.onStart()
//        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().unregister(this)
    }

    private fun snackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun requestVideoAd(cartoon: Cartoon) {
        TapsellPlus.requestRewardedVideoAd(
            this,
            ZONE_ID_REWARD_AD,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    adResponseId = tapsellPlusAdModel.responseId
                    showVideoAd(cartoon)
                }

                override fun error(message: String?) {}
            })
    }

    private fun showVideoAd(cartoon: Cartoon) {
        TapsellPlus.showRewardedVideoAd(this, adResponseId,
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                }

                override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onClosed(tapsellPlusAdModel)
                }

                override fun onRewarded(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onRewarded(tapsellPlusAdModel)

                    startActivity(
                        Intent(
                            this@DetailCartoonActivity,
                            PlayerActivity::class.java
                        ).apply {
                            putExtra(EXTRA_KEY_NAME, cartoon.name)
                            putExtra(EXTRA_KEY_URL, cartoon.url)
                        })
                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    super.onError(tapsellPlusErrorModel)
                }
            })
    }

//    override fun onFavoriteClick(episode: Episode) {
//        viewModel.addEpisodeToFavorites(episode)
//    }

//    override fun onDownloadClick(episode: Episode) {
//
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED
//            ) {
//                startDownloading(episode)
//            } else {
//                requestPermissions()
//            }
//        } else {
//            startDownloading(episode)
//        }
//    }

//    private fun startDownloading(episode: Episode) {
//
//        val filePath =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                .toString() + "/CartoonAbad/" + episode.url.substringAfterLast('/')
//
//        if (File(filePath).exists()) {
//            snackBar(getString(R.string.already_downloaded))
//        } else if (!File(filePath).exists()) {
//
//            val downloadUri = Uri.parse(episode.url)
//            val request = DownloadManager.Request(downloadUri).apply {
//                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
//                setAllowedOverRoaming(false)
//                setTitle(episode.name)
//                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                setDestinationInExternalPublicDir(
//                    Environment.DIRECTORY_DOWNLOADS,
//                    "CartoonAbad" + File.separator + episode.url.substringAfterLast('/')
//                )
//            }
//            val downloadId = downloadManager.enqueue(request)
//
//            addToDownloadDB(episode)
//
//            CoroutineScope(Dispatchers.IO).launch {
//                val query = DownloadManager.Query().setFilterById(downloadId)
//
//                while (isDownload) {
//                    val cursor = downloadManager.query(query)
//                    if (cursor != null && cursor.moveToFirst()) {
//                        val status =
//                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
//                        downloadStatus(status, episode)
//                        cursor.close()
//                    }
//                }
//            }
//
//        }
//    }

//    private fun downloadStatus(status: Int, episode: Episode) {
//
//        when (status) {
//
//            DownloadManager.STATUS_PAUSED -> {
//            }
//            DownloadManager.STATUS_PENDING -> {
//            }
//            DownloadManager.STATUS_RUNNING -> {
//            }
//            DownloadManager.STATUS_FAILED -> {
//            }
//            DownloadManager.STATUS_SUCCESSFUL -> {
//                isDownload = false
//                runOnUiThread {
//                    seasonAdapter?.updateEpisode(episode)
//                }
//            }
//
//            else -> {
//            }
//
//        }
//
//    }

//    private fun requestPermissions() {
//        val hasReadPermission = ContextCompat.checkSelfPermission(
//            this,
//            android.Manifest.permission.READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//
//        val hasWritePermission = ContextCompat.checkSelfPermission(
//            this,
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//
//        readPermissionGranted = hasReadPermission
//        writePermissionGranted = hasWritePermission
//
//        val permissionToRequest = mutableListOf<String>()
//
//        if (!writePermissionGranted) {
//            permissionToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        }
//
//        if (!readPermissionGranted) {
//            permissionToRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//
//        if (permissionToRequest.isNotEmpty()) {
//            permissionLauncher.launch(permissionToRequest.toTypedArray())
//        }
//
//    }

//    private fun addToDownloadDB(episode: Episode) {
//        val path =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                .toString() + "/CartoonAbad/" + episode.url.substringAfterLast('/')
//
//        val downloaded = Downloaded(
//            episode.duration,
//            episode.id,
//            episode.image,
//            episode.name,
//            episode.season_id,
//            path
//        )
//        viewModel.addEpisodeToDownloads(downloaded)
//
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(cartoonAbadEvent: CartoonAbadEvent) {
        when (cartoonAbadEvent.type) {
            CartoonAbadEvent.Type.SIMPLE -> {
                val connectionView = showConnectionLost(true)
                connectionView?.findViewById<MaterialButton>(R.id.btnRetry)?.setOnClickListener {
                    showConnectionLost(false)
                    //we must refresh activity
                }
            }
        }
    }

}