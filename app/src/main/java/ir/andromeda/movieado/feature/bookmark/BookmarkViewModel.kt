package ir.andromeda.movieado.feature.bookmark

import androidx.lifecycle.MutableLiveData
import ir.andromeda.movieado.common.MovieadoCompletableObserver
import ir.andromeda.movieado.common.MovieadoSingleObserver
import ir.andromeda.movieado.common.MovieadoViewModel
import ir.andromeda.movieado.common.asyncNetworkRequest
import ir.andromeda.movieado.data.EmptyState
import ir.andromeda.movieado.data.episode.Episode
import ir.andromeda.movieado.data.episode.EpisodeRepository
import timber.log.Timber

class BookmarkViewModel(private val episodeRepository: EpisodeRepository) :
    MovieadoViewModel() {

    val episodesLiveData = MutableLiveData<List<Episode>>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()

    init {

        emptyStateLiveData.value = EmptyState(false)
        getBookmarkEpisodes()

    }

    fun removeFromBookmark(episode: Episode) {
        episodeRepository.deleteFromBookmark(episode)
            .asyncNetworkRequest()
            .subscribe(object : MovieadoCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("Removed")
                }
            })

        getBookmarkEpisodes()
    }

    private fun getBookmarkEpisodes() {
        episodeRepository.getBookmarkEpisodes()
            .asyncNetworkRequest()
            .subscribe(object : MovieadoSingleObserver<List<Episode>>(compositeDisposable) {
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