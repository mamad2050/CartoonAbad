package ir.andromeda.movieado.feature.allMovie

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.data.movie.MovieRepository

class AllMoviesViewModel(
    val bundle: Bundle,
    private val movieRepository: MovieRepository,
) : MovieadoViewModel() {

    val moviesLiveData = MutableLiveData<List<Movie>>()

    init {
        progressBarLiveData.value = true

        when (bundle.getString(EXTRA_KEY_MODE)) {
            LIST_LATEST_MOVIES -> getMoviesList(SORT_BY_LATEST, TYPE_MOVIE, 1)
            LIST_LATEST_SERIES -> getMoviesList(SORT_BY_LATEST, TYPE_SERIES, 1)
            LIST_LATEST_ANIMATIONS -> getMoviesList(SORT_BY_LATEST, TYPE_ANIMATION, 1)
            LIST_POPULAR_MOVIES -> getMoviesList(SORT_BY_IMDB, TYPE_MOVIE, 1)
            LIST_POPULAR_SERIES -> getMoviesList(SORT_BY_IMDB, TYPE_SERIES, 1)
            LIST_POPULAR_ANIMATIONS -> getMoviesList(SORT_BY_IMDB, TYPE_ANIMATION, 1)
        }
    }

    fun getMoviesList(sort: String, type: String, page: Int) {
        movieRepository.getMovies(sort, type, page)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : MovieadoSingleObserver<List<Movie>>(compositeDisposable) {
                override fun onSuccess(t: List<Movie>) {
                    moviesLiveData.value = t
                }
            })
    }
}