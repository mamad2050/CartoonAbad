package ir.andromeda.cartoonabad.feature.dwnloaded

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadCompletableObserver
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.EmptyState
import ir.andromeda.cartoonabad.data.download.Downloaded
import ir.andromeda.cartoonabad.data.download.DownloadedRepository
import timber.log.Timber

class DownloadedViewModel(private val repository: DownloadedRepository) :
    CartoonAbadViewModel() {

    val downloadedEpisodeLiveData = MutableLiveData<MutableList<Downloaded>>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()

    init {
        emptyStateLiveData.value = EmptyState(false)
        getDownloadedEpisodes()
    }

    fun removeFromDownloads(downloaded: Downloaded) {
        repository.deleteFromDownloads(downloaded)
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("Removed")
                }
            })
        getDownloadedEpisodes()
    }

    private fun getDownloadedEpisodes() {

        repository.getDownloadedEpisodes()
            .asyncNetworkRequest()
            .subscribe(object :
                CartoonAbadSingleObserver<List<Downloaded>>(compositeDisposable) {
                override fun onSuccess(t: List<Downloaded>) {
                    try {
                        if (t.isNotEmpty()) {
                            downloadedEpisodeLiveData.value = t as MutableList<Downloaded>
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