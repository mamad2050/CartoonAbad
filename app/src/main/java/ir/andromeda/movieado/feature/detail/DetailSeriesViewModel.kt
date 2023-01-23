package ir.andromeda.movieado.feature.detail

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.schedulers.Schedulers
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.download.DownloadRepository
import ir.andromeda.movieado.data.episode.Episode
import ir.andromeda.movieado.data.episode.EpisodeRepository
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.data.movie.MovieRepository
import ir.andromeda.movieado.data.season.Season
import ir.andromeda.movieado.data.season.SeasonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailSeriesViewModel(
    bundle: Bundle,
    private val seriesRepository: MovieRepository,
    private val seasonRepository: SeasonRepository,
    private val episodeRepository: EpisodeRepository,
    private val downloadRepository: DownloadRepository
) :
    MovieadoViewModel() {

    val seriesLiveData = MutableLiveData<Movie>()
    val seasonsLiveData = MutableLiveData<List<Season>>()
    val episodesLiveData = MutableLiveData<List<Episode>>()

    private val movieId: String

    init {
        movieId = bundle.getString(EXTRA_KEY_ID) ?: ""
        viewModelScope.launch {
            showEpisodes()
            delay(200)
            getSeries()
        }
    }

    fun getSeries() {
        progressBarLiveData.value = true

        seriesRepository.getMovieDetail(movieId)
            .asyncNetworkRequest()
            .subscribe(object : MovieadoSingleObserver<Movie>(compositeDisposable) {
                override fun onSuccess(t: Movie) {
                    seriesLiveData.value = t
                }
            })

        seasonRepository.getSeasons(movieId)
            .doFinally { progressBarLiveData.postValue(false) }
            .asyncNetworkRequest()
            .subscribe(object : MovieadoSingleObserver<List<Season>>(compositeDisposable) {
                override fun onSuccess(t: List<Season>) {
                    seasonsLiveData.value = t
                }
            })
    }

    fun showEpisodes() {
        episodeRepository.getAllEpisodes(movieId)
            .asyncNetworkRequest()
            .subscribe(object : MovieadoSingleObserver<List<Episode>>(compositeDisposable) {
                override fun onSuccess(t: List<Episode>) {
                    episodesLiveData.value = t
                }
            })
    }

    fun addEpisodeToBookmarks(episode: Episode) {
        if (episode.isBookmarked)
            episodeRepository.deleteFromBookmark(episode)
                .subscribeOn(Schedulers.io())
                .subscribe(object : MovieadoCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        episode.isBookmarked = false
                    }
                })
        else
            episodeRepository.addToBookmark(episode)
                .subscribeOn(Schedulers.io())
                .subscribe(object : MovieadoCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        episode.isBookmarked = true
                    }
                })
    }

//    fun addEpisodeToDownloads(episode: Episode) {
//        if (episode.isDownloaded)
//            downloadRepository.deleteFromDownloads(episode)
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
//                    override fun onComplete() {
//                        episode.isDownload = false
//                    }
//                })
//        else
//            downloadRepository.addToDownloads(episode)
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
//                    override fun onComplete() {
//                        episode.isDownload = true
//                    }
//                })
//    }
}

