package ir.andromeda.cartoonabad.feature.dwnloaded

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadCompletableObserver
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.EmptyState
import ir.andromeda.cartoonabad.data.downloaded.DownloadedEpisode
import ir.andromeda.cartoonabad.data.downloaded.DownloadedEpisodeRepository
import ir.andromeda.cartoonabad.data.episode.Episode
import timber.log.Timber

class DownloadedViewModel(private val episodeRepository: DownloadedEpisodeRepository) :
    CartoonAbadViewModel() {


    val downloadedEpisodeLiveData = MutableLiveData<List<DownloadedEpisode>>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()

    init {
        emptyStateLiveData.value = EmptyState(false)
        getDownloadedEpisodes()
    }

    fun removeFromDownloads(episode: DownloadedEpisode) {
        episodeRepository.deleteFromDownloads(episode)
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("Removed")
                }
            })
        getDownloadedEpisodes()
    }

    private fun getDownloadedEpisodes() {

        episodeRepository.getDownloadedEpisodes()
            .asyncNetworkRequest()
            .subscribe(object :
                CartoonAbadSingleObserver<List<DownloadedEpisode>>(compositeDisposable) {
                override fun onSuccess(t: List<DownloadedEpisode>) {
                    if (t.isNotEmpty()) {
                        downloadedEpisodeLiveData.value = t
                    } else {
                        emptyStateLiveData.value = EmptyState(
                            true,
                        )
                    }
                }
            })
    }

}