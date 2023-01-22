package ir.andromeda.movieado.feature.search

import androidx.lifecycle.MutableLiveData
import ir.andromeda.movieado.common.MovieadoSingleObserver
import ir.andromeda.movieado.common.MovieadoViewModel
import ir.andromeda.movieado.common.asyncNetworkRequest
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.data.movie.MovieRepository

class SearchViewModel(private val movieRepository: MovieRepository) :
    MovieadoViewModel() {

    val searchLiveData = MutableLiveData<List<Movie>>()
    val showNotFoundState = MutableLiveData<Boolean>()

    fun search(query: String) {
        progressBarLiveData.value = true
        showNotFoundState.value = false
        movieRepository.searchMovie(query)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object :
                MovieadoSingleObserver<List<Movie>>(compositeDisposable) {
                override fun onSuccess(t: List<Movie>) {
                    if (t.isEmpty())
                        showNotFoundState.value = true
                    else
                        searchLiveData.value = t
                }
            })
    }

}