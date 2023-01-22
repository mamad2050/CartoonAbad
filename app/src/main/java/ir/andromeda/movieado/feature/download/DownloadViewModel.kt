package ir.andromeda.movieado.feature.download

import androidx.lifecycle.MutableLiveData
import ir.andromeda.movieado.common.MovieadoCompletableObserver
import ir.andromeda.movieado.common.MovieadoSingleObserver
import ir.andromeda.movieado.common.MovieadoViewModel
import ir.andromeda.movieado.common.asyncNetworkRequest
import ir.andromeda.movieado.data.EmptyState
import ir.andromeda.movieado.data.download.Download
import ir.andromeda.movieado.data.download.DownloadRepository
import timber.log.Timber

class DownloadViewModel(private val repository: DownloadRepository) :
    MovieadoViewModel() {

    val downloadEpisodeLiveData = MutableLiveData<MutableList<Download>>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()

    init {
        emptyStateLiveData.value = EmptyState(false)
        getDownloadedEpisodes()
    }

    fun removeFromDownloads(download: Download) {
        repository.deleteFromDownloads(download)
            .asyncNetworkRequest()
            .subscribe(object : MovieadoCompletableObserver(compositeDisposable) {
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
                MovieadoSingleObserver<List<Download>>(compositeDisposable) {
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