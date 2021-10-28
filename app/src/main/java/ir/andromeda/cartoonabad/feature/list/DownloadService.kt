package ir.andromeda.cartoonabad.feature.list

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import com.downloader.*
import ir.andromeda.cartoonabad.common.toMB
import ir.andromeda.cartoonabad.data.downloaded.Downloaded
import ir.andromeda.cartoonabad.data.episode.Episode
import java.io.File


class DownloadService(

) : Service() {

    private val binder: IBinder = EpisodeBinder()
    private var downloadId = 0

    lateinit var listener: OnDownloadEventListener

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }


    inner class EpisodeBinder : Binder() {

        fun getService(): DownloadService {

            return DownloadService()
        }

    }

    fun startDownload(episode: Episode) {

//        val episodePath = episode.url.substringAfterLast('/')
//
//        downloadId =
//            PRDownloader.download(episode.url, dirPath, episodePath)
//                .build()
//                .setOnStartOrResumeListener {
//                    listener.onDownloadStarted(episode.name)
//                }
//
//                .setOnProgressListener { progress ->
//
//                    val progressPercent = progress.currentBytes * 100 / progress.totalBytes
//                    listener.onProgressListener(
//                        toMB(progress.currentBytes) + "/" + toMB(progress.totalBytes),
//                        progressPercent
//                    )
//
//                }
//                .setOnCancelListener {
//                    listener.onDownloadCanceled()
//                }
//                .setOnPauseListener {
//
//                }
//                .start(object : OnDownloadListener {
//                    override fun onDownloadComplete() {
//
//                        val downloaded = Downloaded(
//                            episode.duration,
//                            episode.id,
//                            episode.image,
//                            episode.name,
//                            episode.season_id,
//                            episodePath
//                        )
//
//                        listener.onDownloadCompleted(downloaded)
//                    }
//
//                    override fun onError(error: Error?) {
//                        listener.onErrorDownload()
//                    }
//
//                })
    }

    interface OnDownloadEventListener {

        fun onDownloadStarted(episodeName: String)
        fun onDownloadCompleted(downloadedEpisode: Downloaded)
        fun onDownloadCanceled()
        fun onErrorDownload()
        fun onProgressListener(percent: String, p: Long)
    }

}


