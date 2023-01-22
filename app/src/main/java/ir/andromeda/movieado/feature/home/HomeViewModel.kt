package ir.andromeda.movieado.feature.home

import androidx.lifecycle.MutableLiveData
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.banner.Banner
import ir.andromeda.movieado.data.banner.BannerRepository
import ir.andromeda.movieado.data.genre.Genre
import ir.andromeda.movieado.data.genre.GenreRepository
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.data.movie.MovieRepository

class HomeViewModel(
    genreRepository: GenreRepository,
    movieRepository: MovieRepository,
    bannerRepository: BannerRepository
) : MovieadoViewModel() {

    val bannersLiveData = MutableLiveData<List<Banner>>()
    val genresLiveData = MutableLiveData<List<Genre>>()
    val latestMoviesLiveData = MutableLiveData<List<Movie>>()
    val latestSeriesLiveData = MutableLiveData<List<Movie>>()
    val latestAnimationsLiveData = MutableLiveData<List<Movie>>()
    val popularMoviesLiveData = MutableLiveData<List<Movie>>()
    val popularSeriesLiveData = MutableLiveData<List<Movie>>()
    val popularAnimationsLiveData = MutableLiveData<List<Movie>>()

    init {
        progressBarLiveData.value = true

        bannerRepository.getBanners().asyncNetworkRequest()
            .subscribe(object : MovieadoSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    bannersLiveData.value = t
                }

            })

        genreRepository.getGenres().asyncNetworkRequest()
            .subscribe(object : MovieadoSingleObserver<List<Genre>>(compositeDisposable) {
                override fun onSuccess(t: List<Genre>) {
                    genresLiveData.value = t
                }
            })

        movieRepository.getMovies(SORT_BY_LATEST, TYPE_MOVIE, 1).asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : MovieadoSingleObserver<List<Movie>>(compositeDisposable) {
                override fun onSuccess(t: List<Movie>) {
                    latestMoviesLiveData.value = t
                }
            })

        movieRepository.getMovies(SORT_BY_LATEST, TYPE_SERIES, 1).asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : MovieadoSingleObserver<List<Movie>>(compositeDisposable) {
                override fun onSuccess(t: List<Movie>) {
                    latestSeriesLiveData.value = t
                }
            })

        movieRepository.getMovies(SORT_BY_LATEST, TYPE_ANIMATION, 1).asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : MovieadoSingleObserver<List<Movie>>(compositeDisposable) {
                override fun onSuccess(t: List<Movie>) {
                    latestAnimationsLiveData.value = t
                }
            })

        movieRepository.getMovies(SORT_BY_IMDB, TYPE_MOVIE, 1).asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : MovieadoSingleObserver<List<Movie>>(compositeDisposable) {
                override fun onSuccess(t: List<Movie>) {
                    popularMoviesLiveData.value = t
                }
            })

        movieRepository.getMovies(SORT_BY_IMDB, TYPE_SERIES, 1).asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : MovieadoSingleObserver<List<Movie>>(compositeDisposable) {
                override fun onSuccess(t: List<Movie>) {
                    popularSeriesLiveData.value = t
                }
            })

        movieRepository.getMovies(SORT_BY_IMDB, TYPE_ANIMATION, 1).asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : MovieadoSingleObserver<List<Movie>>(compositeDisposable) {
                override fun onSuccess(t: List<Movie>) {
                    popularAnimationsLiveData.value = t
                }
            })
    }
}