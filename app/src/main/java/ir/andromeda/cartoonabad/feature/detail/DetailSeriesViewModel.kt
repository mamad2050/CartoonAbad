package ir.andromeda.cartoonabad.feature.detail

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.animation.Animation
import ir.andromeda.cartoonabad.data.download.Downloaded
import ir.andromeda.cartoonabad.data.download.DownloadedRepository
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.data.episode.EpisodeRepository
import ir.andromeda.cartoonabad.data.season.Season
import ir.andromeda.cartoonabad.data.season.SeasonRepository

class DetailSeriesViewModel(
    private val bundle: Bundle,
    private val seasonRepository: SeasonRepository,
    private val episodeRepository: EpisodeRepository,
    private val downloadRepository: DownloadedRepository
) :
    CartoonAbadViewModel() {

    val seasonsLiveData = MutableLiveData<List<Season>>()
    val animationLiveData = MutableLiveData<Animation>()

    init {
        showSeasons()
    }

    fun showSeasons() {
        progressBarLiveData.value = true

        animationLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)

        seasonRepository.getSeasons(animationLiveData.value!!.id.toInt())
            .doFinally { progressBarLiveData.postValue(false) }
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Season>>(compositeDisposable) {
                override fun onSuccess(t: List<Season>) {
                    seasonsLiveData.value = t
                }
            })
    }

    fun addEpisodeToFavorites(episode: Episode) {
        if (episode.isFavorite)
            episodeRepository.deleteFromFavorite(episode)
                .subscribeOn(Schedulers.io())
                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        episode.isFavorite = false
                    }
                })
        else
            episodeRepository.addToFavorite(episode)
                .subscribeOn(Schedulers.io())
                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        episode.isFavorite = true
                    }
                })
    }

    fun addEpisodeToDownloads(episode: Downloaded) {
        if (episode.isDownload)
            downloadRepository.deleteFromDownloads(episode)
                .subscribeOn(Schedulers.io())
                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        episode.isDownload = false
                    }
                })
        else
            downloadRepository.addToDownloads(episode)
                .subscribeOn(Schedulers.io())
                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        episode.isDownload = true
                    }
                })
    }
}

