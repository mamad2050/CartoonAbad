package ir.andromeda.movieado.feature.search

import androidx.lifecycle.MutableLiveData
import ir.andromeda.movieado.common.MovieadoSingleObserver
import ir.andromeda.movieado.common.MovieadoViewModel
import ir.andromeda.movieado.common.asyncNetworkRequest
import ir.andromeda.movieado.data.genre.Genre
import ir.andromeda.movieado.data.genre.GenreRepository

class FilterFragmentViewModel(
    genreRepository: GenreRepository
) : MovieadoViewModel() {

    val genresLiveData = MutableLiveData<List<Genre>>()

    init {
        genreRepository.getGenres()
            .asyncNetworkRequest()
            .subscribe(object : MovieadoSingleObserver<List<Genre>>(compositeDisposable) {
                override fun onSuccess(t: List<Genre>) {
                    genresLiveData.value = t
                }
            })
    }
}