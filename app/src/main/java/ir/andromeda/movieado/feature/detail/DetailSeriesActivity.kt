package ir.andromeda.movieado.feature.detail

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import ir.andromeda.movieado.R
import ir.andromeda.movieado.common.EXTRA_KEY_NAME
import ir.andromeda.movieado.common.EXTRA_KEY_URL
import ir.andromeda.movieado.common.MovieadoActivity
import ir.andromeda.movieado.data.MovieadoEvent
import ir.andromeda.movieado.data.episode.Episode
import ir.andromeda.movieado.data.season.Season
import ir.andromeda.movieado.databinding.ActivityDetailSeriesBinding
import ir.andromeda.movieado.feature.player.PlayerActivity
import ir.andromeda.movieado.services.imageloader.ImageLoadingService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailSeriesActivity : MovieadoActivity(), EpisodeEventListener {

    private lateinit var binding: ActivityDetailSeriesBinding
    private val imageLoadingService: ImageLoadingService by inject()
    private val detailSeriesViewModel: DetailSeriesViewModel by viewModel { parametersOf(intent.extras) }

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private lateinit var downloadManager: DownloadManager
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val episodeAdapter by lazy { EpisodeAdapter(imageLoadingService, this) }
    private val genreAdapter by lazy { GenreInDetailAdapter() }

    private var selectedSeasonId: String = ""
    private var episodeList = listOf<Episode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSeriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        detailSeriesViewModel.seriesLiveData.observe(this) {
            binding.tvName.text = it.name
            binding.tvRate.text = "امتیاز ${it.imdb} / 10"
            binding.tvDescription.text = it.description
            imageLoadingService.load(binding.ivImage, it.banner)
            genreAdapter.genres = it.genres as ArrayList<String>
        }

        detailSeriesViewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        detailSeriesViewModel.episodesLiveData.observe(this) {
            episodeList = it
        }

        detailSeriesViewModel.seasonsLiveData.observe(this) {
            binding.tvSeasonSize.text = "${it.size} فصل"
            val adapter = ArrayAdapter(this, R.layout.item_topic, it)
            binding.autoTvSeason.setAdapter(adapter)
            binding.autoTvSeason.setText(binding.autoTvSeason.adapter.getItem(0).toString(), false)
            selectedSeasonId = it[0].id
            episodeAdapter.episodes = episodeList.filter { episode ->
                episode.seasonId == selectedSeasonId
            } as ArrayList<Episode>
        }

        binding.autoTvSeason.setOnItemClickListener { parent, _, position, _ ->
            val temp = (parent.getItemAtPosition(position) as Season).id
            //TODO show small progressbar
            if (selectedSeasonId != temp) {
                selectedSeasonId = temp
                episodeAdapter.episodes = episodeList.filter { episode ->
                    episode.seasonId == selectedSeasonId
                } as ArrayList<Episode>
            }
        }


        binding.rvEpisodes.adapter = episodeAdapter
        binding.rvEpisodes.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.rvGenre.adapter = genreAdapter
        binding.rvGenre.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)


        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
            { permission ->
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

    private fun snackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onEpisodeClicked(episode: Episode) {
        startActivity(Intent(this, PlayerActivity::class.java).apply {
            putExtra(EXTRA_KEY_NAME, episode.name)
            putExtra(EXTRA_KEY_URL, episode.url)
        })
    }

    override fun onFavoriteClicked(episode: Episode) {
        detailSeriesViewModel.addEpisodeToBookmarks(episode)
    }

    override fun onDownloadClicked(episode: Episode) {

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
    }

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
////                    seasonAdapter?.updateEpisode(episode)
//                }
//            }
//
//            else -> {
//            }
//
//        }
//
//    }
//
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
//        val download = Download(
//            episode.duration,
//            episode.id,
//            episode.imageUrl,
//            episode.name,
//            path
//        )
//        viewModel.addEpisodeToDownloads(download)
//
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(movieadoEvent: MovieadoEvent) {
        when (movieadoEvent.type) {
            MovieadoEvent.Type.SIMPLE -> {
                val connectionView = showConnectionLost(true)
                connectionView?.findViewById<MaterialButton>(R.id.btnRetry)?.setOnClickListener {
                    showConnectionLost(false)
                    detailSeriesViewModel.getSeries()
                }
            }
            else -> {}
        }
    }

}