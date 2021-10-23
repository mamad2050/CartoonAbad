package ir.andromeda.cartoonabad.feature.list

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import ir.andromeda.cartoonabad.data.episode.Episode
import java.io.File


class DownloadService(

) : Service(), OnDownloadListener {

    private lateinit var config: PRDownloaderConfig
    private val binder: IBinder = EpisodeBinder()
    private var downloadId = 0
    val episodeList = ArrayList<Episode>()

    override fun onCreate() {
        super.onCreate()

        config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(baseContext, config)



    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }


    override fun onDownloadComplete() {
//        Toast.makeText(baseContext, "COMPLETE", Toast.LENGTH_SHORT).show()

    }

    override fun onError(error: Error?) {
//        Toast.makeText(baseContext, "ERROR", Toast.LENGTH_SHORT).show()

    }


    inner class EpisodeBinder : Binder() {

        fun getService(): DownloadService {

            return DownloadService()
        }

    }

     fun startDownload(episode: Episode){

          val dirPath =
             Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath +
                     File.separator + "CartoonAbad" + File.separator
        downloadId =
            PRDownloader.download(episode.url, dirPath, episode.url.substringAfterLast('/'))
                .build()
                .setOnStartOrResumeListener {
//                dialog.setTitle("Started")
//                dialog.show()
//                    Toast.makeText(baseContext, "START", Toast.LENGTH_SHORT).show()
                }
                .setOnProgressListener { progress ->
                    val progressPercent = progress.currentBytes * 100 / progress.totalBytes
//                dialog.progress = progressPercent.toInt()
//                dialog.setMessage(toMB(progress.currentBytes) + "/" + toMB(progress.totalBytes))

                }
                .setOnCancelListener {
                }
                .setOnPauseListener {

                }
                .start(this)

    }


}