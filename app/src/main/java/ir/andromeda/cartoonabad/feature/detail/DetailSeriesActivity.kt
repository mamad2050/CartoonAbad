package ir.andromeda.cartoonabad.feature.detail

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.PurchaseContainer
import ir.andromeda.cartoonabad.data.download.Downloaded
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.databinding.ActivityDetailSeriesBinding
import ir.andromeda.cartoonabad.feature.player.PlayerActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.view.CartoonAbadImageView
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

class DetailSeriesActivity : CartoonAbadActivity(), EpisodeEventListener {

    private lateinit var binding: ActivityDetailSeriesBinding
    private var seasonAdapter: SeasonAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: DetailSeriesViewModel by viewModel { parametersOf(intent.extras) }

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private lateinit var downloadManager: DownloadManager
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var adResponseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSeriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        viewModel.animationLiveData.observe(this) {
            binding.tvName.text = it.name
            binding.tvSeasonSize.text = "${it.no_seasons} فصل"
            binding.tvRate.text = "امتیاز ${it.rate} / 10"
//            binding.tvDescription.text = it.description
            imageLoadingService.load(binding.ivImage as CartoonAbadImageView, it.image)
        }

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        viewModel.seasonsLiveData.observe(this) {
            it.forEach { seasons ->
                seasons.episodeList.forEach { episode ->
                    val filePath =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .toString() + "/CartoonAbad/" + episode.url.substringAfterLast('/')
                    if (File(filePath).exists()) {
                        episode.isDownloaded = true
                    }
                }
            }

            //setup adapter
            binding.rvSeasons.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            seasonAdapter = SeasonAdapter(it, imageLoadingService, this, this)
            binding.rvSeasons.adapter = seasonAdapter
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
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()

        FirebaseAnalytics.getInstance(this)
            .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, "ListFragment")
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.javaClass.simpleName)
            })
    }

    private fun snackBar(message: String) {
        Snackbar.make(binding.rootLayout, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onEpisodeClick(episode: Episode) {

        if (PurchaseContainer.purchaseInfo == null) {
            requestVideoAd(episode)
        } else {
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra(EXTRA_KEY_NAME, episode.name)
                putExtra(EXTRA_KEY_URL, episode.url)
            })
        }
    }

    private fun requestVideoAd(episode: Episode) {
        TapsellPlus.requestRewardedVideoAd(
            this,
            ZONE_ID_REWARD_AD,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    adResponseId = tapsellPlusAdModel.responseId
                    showVideoAd(episode)
                }

                override fun error(message: String?) {}
            })
    }

    private fun showVideoAd(episode: Episode) {
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
                            this@DetailSeriesActivity,
                            PlayerActivity::class.java
                        ).apply {
                            putExtra(EXTRA_KEY_NAME, episode.name)
                            putExtra(EXTRA_KEY_URL, episode.url)
                        })
                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    super.onError(tapsellPlusErrorModel)
                }
            })
    }

    override fun onFavoriteClick(episode: Episode) {
        viewModel.addEpisodeToFavorites(episode)
    }

    override fun onDownloadClick(episode: Episode) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        } else if (!File(filePath).exists()) {

            val downloadUri = Uri.parse(episode.url)
            val request = DownloadManager.Request(downloadUri).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                setAllowedOverRoaming(false)
                setTitle(episode.name)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "CartoonAbad" + File.separator + episode.url.substringAfterLast('/')
                )
            }
            val downloadId = downloadManager.enqueue(request)

            addToDownloadDB(episode)

            CoroutineScope(Dispatchers.IO).launch {
                val query = DownloadManager.Query().setFilterById(downloadId)

                while (isDownload) {
                    val cursor = downloadManager.query(query)
                    if (cursor != null && cursor.moveToFirst()) {
                        val status =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        downloadStatus(status, episode)
                        cursor.close()
                    }
                }
            }

        }
    }

    private fun downloadStatus(status: Int, episode: Episode) {

        when (status) {

            DownloadManager.STATUS_PAUSED -> {
            }
            DownloadManager.STATUS_PENDING -> {
            }
            DownloadManager.STATUS_RUNNING -> {
            }
            DownloadManager.STATUS_FAILED -> {
            }
            DownloadManager.STATUS_SUCCESSFUL -> {
                isDownload = false
                runOnUiThread {
                    seasonAdapter?.updateEpisode(episode)
                }
            }

            else -> {
            }

        }

    }

    private fun requestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
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

    private fun addToDownloadDB(episode: Episode) {
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
        }
    }

    private fun requestBannerAd() {
        TapsellPlus.requestInterstitialAd(
            this,
            ZONE_ID_INTERSTITIAL_BANNER_AD,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    adResponseId = tapsellPlusAdModel.responseId
                    showBannerAd()
                }

                override fun error(message: String?) {}
            })
    }

    private fun showBannerAd() {

        TapsellPlus.showInterstitialAd(this, adResponseId,
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                }

                override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onClosed(tapsellPlusAdModel)
                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    super.onError(tapsellPlusErrorModel)
                }
            })
    }

}