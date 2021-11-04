package ir.andromeda.cartoonabad.feature.list

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ir.andromeda.cartoonabad.common.CartoonAbadCompletableObserver
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.downloaded.Downloaded
import ir.andromeda.cartoonabad.data.downloaded.DownloadedRepository
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.data.episode.EpisodeRepository
import ir.andromeda.cartoonabad.data.season.Season
import ir.andromeda.cartoonabad.data.season.SeasonRepository
import timber.log.Timber

class ListViewModel(
    private val animationId: String,
    private val seasonRepository: SeasonRepository,
    private val episodeRepository: EpisodeRepository,
    private val downloadRepository: DownloadedRepository
) :
    CartoonAbadViewModel() {

    val seasonsLiveData = MutableLiveData<List<Season>>()
    val downloadsLiveData = MutableLiveData<List<Downloaded>>()

    init {
        downloadRepository.getDownloadedEpisodes()
            .doFinally { progressBarLiveData.postValue(false) }
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Downloaded>>(compositeDisposable) {
                override fun onSuccess(t: List<Downloaded>) {
                    downloadsLiveData.value = t
                }
            })

        showSeasons()
    }

    fun showSeasons() {
        progressBarLiveData.value = true

        seasonRepository.getSeasons(animationId.toInt())
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

