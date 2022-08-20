package ir.andromeda.cartoonabad.feature.bookmark

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadCompletableObserver
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.EmptyState
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.data.episode.EpisodeRepository
import timber.log.Timber

class BookmarkViewModel(private val episodeRepository: EpisodeRepository) :
    CartoonAbadViewModel() {

    val episodesLiveData = MutableLiveData<List<Episode>>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()

    init {

        emptyStateLiveData.value = EmptyState(false)
        getBookmarkEpisodes()

    }

    fun removeFromBookmark(episode: Episode) {
        episodeRepository.deleteFromBookmark(episode)
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("Removed")
                }
            })

        getBookmarkEpisodes()
    }

    private fun getBookmarkEpisodes() {
        episodeRepository.getBookmarkEpisodes()
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Episode>>(compositeDisposable) {
                override fun onSuccess(t: List<Episode>) {
                    if (t.isNotEmpty()) {
                        episodesLiveData.value = t
                    } else {
                        emptyStateLiveData.value = EmptyState(
                            true,
                        )
                    }

                }
            })
    }

}