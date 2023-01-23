package ir.andromeda.movieado.feature.detail

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.download.DownloadRepository
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.data.movie.MovieRepository

class DetailMovieViewModel(
    bundle: Bundle,
    private val movieRepository: MovieRepository,
    private val downloadRepository: DownloadRepository
) :
    MovieadoViewModel() {

    val movieLiveData = MutableLiveData<Movie>()
    private val movieId: String

    init {
        movieId = bundle.getString(EXTRA_KEY_ID) ?: ""
        getMovie()
    }

    fun getMovie() {
        progressBarLiveData.value = true
        movieRepository.getMovieDetail(movieId)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : MovieadoSingleObserver<Movie>(compositeDisposable) {
                override fun onSuccess(t: Movie) {
                    movieLiveData.value = t
                }

            })
    }

//    fun addEpisodeToFavorites(episode: Episode) {
//        if (episode.isFavorite)
//            episodeRepository.deleteFromFavorite(episode)
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
//                    override fun onComplete() {
//                        episode.isFavorite = false
//                    }
//                })
//        else
//            episodeRepository.addToFavorite(episode)
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
//                    override fun onComplete() {
//                        episode.isFavorite = true
//                    }
//                })
//    }
//
//    fun addEpisodeToDownloads(episode: Downloaded) {
//        if (episode.isDownload)
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

