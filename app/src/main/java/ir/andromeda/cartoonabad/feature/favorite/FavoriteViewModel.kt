package ir.andromeda.cartoonabad.feature.favorite

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadCompletableObserver
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.data.episode.EpisodeRepository
import timber.log.Timber

class FavoriteViewModel(private val episodeRepository: EpisodeRepository) :
    CartoonAbadViewModel() {

    val episodesLiveData = MutableLiveData<List<Episode>>()

    init {

        episodeRepository.getFavoriteEpisodes()
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Episode>>(compositeDisposable) {
                override fun onSuccess(t: List<Episode>) {
                    episodesLiveData.value = t
                }
            })
    }


    fun removeFromFavorite(episode: Episode) {
        episodeRepository.deleteFromFavorite(episode)
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("Removed")
                }
            })
    }

}