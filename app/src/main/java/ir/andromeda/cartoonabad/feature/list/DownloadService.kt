package ir.andromeda.cartoonabad.feature.list

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.NOTIFICATION_CHANNEL_ID
import ir.andromeda.cartoonabad.common.PENDING_INTENT_CODE
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.feature.main.MainActivity
import java.io.File


class DownloadService(

) : Service(), OnDownloadListener {

    private val binder: IBinder = EpisodeBinder()
    private var downloadId = 0
    private val dirPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath +
                File.separator + "CartoonAbad" + File.separator

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

        downloadId =
            PRDownloader.download(episode.url, dirPath, episode.url.substringAfterLast('/'))
                .build()
                .setOnStartOrResumeListener {

                    listener.onDownloadStarted()

                }
                .setOnProgressListener { progress ->

                    val progressPercent = progress.currentBytes * 100 / progress.totalBytes

//                dialog.setMessage(toMB(progress.currentBytes) + "/" + toMB(progress.totalBytes))

                }
                .setOnCancelListener {
                    listener.onDownloadCanceled()
                }
                .setOnPauseListener {

                }
                .start(this)

    }


    override fun onDownloadComplete() {
        listener.onDownloadCompleted()

    }

    override fun onError(error: Error?) {
        listener.onErrorDownload()
    }

    interface OnDownloadEventListener {

        fun onDownloadStarted()
        fun onDownloadCompleted()
        fun onDownloadCanceled()
        fun onErrorDownload()

    }

}


