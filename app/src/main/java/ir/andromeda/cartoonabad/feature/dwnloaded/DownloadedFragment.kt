package ir.andromeda.cartoonabad.feature.dwnloaded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Func
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.feature.main.DrawerLocker
import kotlin.Error

class DownloadedFragment() : CartoonAbadFragment() {

    private lateinit var fetch: Fetch
    private lateinit var fetchConfiguration: FetchConfiguration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_downloaded, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as DrawerLocker).setDrawerLocked(true)


//        fetchConfiguration = FetchConfiguration.Builder(requireContext())
//            .setDownloadConcurrentLimit(1)
//            .build()
//
//        fetch = Fetch.Impl.getInstance(fetchConfiguration)
//
//        val address = "/downloads/test.txt"
//        val request = Request(episode.url, address)
//
//        request.priority = Priority.HIGH
//        request.networkType = NetworkType.ALL
//        request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG")
//        fetch.enqueue(request,
//            {
//            }
//        ) {
//        }
//
//        val fetchListener = object : FetchListener {
//            override fun onAdded(download: Download) {
//
//            }
//
//            override fun onCancelled(download: Download) {
//
//            }
//
//            override fun onCompleted(download: Download) {
//
//            }
//
//            override fun onDeleted(download: Download) {
//
//            }
//
//            override fun onDownloadBlockUpdated(
//                download: Download,
//                downloadBlock: DownloadBlock,
//                totalBlocks: Int
//            ) {
//
//            }
//
//            override fun onError(
//                download: Download,
//                error: com.tonyodev.fetch2.Error,
//                throwable: Throwable?
//            ) {
//                val error = download.error
//            }
//
//            override fun onPaused(download: Download) {
//
//            }
//
//            override fun onProgress(
//                download: Download,
//                etaInMilliSeconds: Long,
//                downloadedBytesPerSecond: Long
//            ) {
//                if (request.id === download.id) {
////                    updateDownload(download, etaInMilliSeconds)
//                }
//                val progress: Int = download.progress
//            }
//
//            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
//                if (request.id == download.id) {
////                    showDownloadInList(download);
//                }
//            }
//
//            override fun onRemoved(download: Download) {
//
//            }
//
//            override fun onResumed(download: Download) {
//
//            }
//
//            override fun onStarted(
//                download: Download,
//                downloadBlocks: List<DownloadBlock>,
//                totalBlocks: Int
//            ) {
//
//            }
//
//            override fun onWaitingNetwork(download: Download) {
//
//            }
//
//        }
//
//        fetch.addListener(fetchListener)


    }

    override fun onStop() {
        super.onStop()
        (activity as DrawerLocker).setDrawerLocked(false)
    }
}