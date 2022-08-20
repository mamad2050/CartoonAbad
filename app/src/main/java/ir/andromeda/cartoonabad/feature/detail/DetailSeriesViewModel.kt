package ir.andromeda.cartoonabad.feature.detail

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.download.Download
import ir.andromeda.cartoonabad.data.download.DownloadRepository
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.data.episode.EpisodeRepository
import ir.andromeda.cartoonabad.data.season.Season
import ir.andromeda.cartoonabad.data.season.SeasonRepository
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.data.series.SeriesRepository

class DetailSeriesViewModel(
    private val bundle: Bundle,
    private val seriesRepository: SeriesRepository,
    private val seasonRepository: SeasonRepository,
    private val episodeRepository: EpisodeRepository,
    private val downloadRepository: DownloadRepository
) :
    CartoonAbadViewModel() {

    val seriesLiveData = MutableLiveData<Series>()
    val seasonsLiveData = MutableLiveData<List<Season>>()
    val episodesLiveData = MutableLiveData<List<Episode>>()

    private var seriesId = ""


    init {
        seriesId = bundle.getString(EXTRA_KEY_ID) ?: ""
        showSeasons()
        showEpisodes()
    }

    fun showSeasons() {
        progressBarLiveData.value = true

        seriesRepository.getSeriesDetail(seriesId)
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<Series>(compositeDisposable) {
                override fun onSuccess(t: Series) {
                    seriesLiveData.value = t
                }
            })

        seasonRepository.getSeasons(seriesId)
            .doFinally { progressBarLiveData.postValue(false) }
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Season>>(compositeDisposable) {
                override fun onSuccess(t: List<Season>) {
                    seasonsLiveData.value = t
                }
            })
    }

    fun showEpisodes() {
        episodeRepository.getAllEpisodes(seriesId)
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Episode>>(compositeDisposable) {
                override fun onSuccess(t: List<Episode>) {
                    episodesLiveData.value = t
                }
            })
    }

    fun addEpisodeToFavorites(episode: Episode) {
        if (episode.isBookmarked)
            episodeRepository.deleteFromBookmark(episode)
                .subscribeOn(Schedulers.io())
                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        episode.isBookmarked = false
                    }
                })
        else
            episodeRepository.addToBookmark(episode)
                .subscribeOn(Schedulers.io())
                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        episode.isBookmarked = true
                    }
                })
    }

    fun addEpisodeToDownloads(episode: Download) {
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

