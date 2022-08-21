package ir.andromeda.cartoonabad.feature.download

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadCompletableObserver
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.EmptyState
import ir.andromeda.cartoonabad.data.download.Download
import ir.andromeda.cartoonabad.data.download.DownloadRepository
import timber.log.Timber

class DownloadViewModel(private val repository: DownloadRepository) :
    CartoonAbadViewModel() {

    val downloadEpisodeLiveData = MutableLiveData<MutableList<Download>>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()

    init {
        emptyStateLiveData.value = EmptyState(false)
        getDownloadedEpisodes()
    }

    fun removeFromDownloads(download: Download) {
        repository.deleteFromDownloads(download)
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("Item Removed")
                }
            })
        getDownloadedEpisodes()
    }

    private fun getDownloadedEpisodes() {

        repository.getDownloadedEpisodes()
            .asyncNetworkRequest()
            .subscribe(object :
                CartoonAbadSingleObserver<List<Download>>(compositeDisposable) {
                override fun onSuccess(t: List<Download>) {
                    try {
                        if (t.isNotEmpty()) {
                            downloadEpisodeLiveData.value = t as MutableList<Download>
                        } else {
                            emptyStateLiveData.value = EmptyState(
                                true,
                            )
                        }
                    } catch (error: Throwable) {
                        Timber.e(error)
                    }
                }
            })
    }
}