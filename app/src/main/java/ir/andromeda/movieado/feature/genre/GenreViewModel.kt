package ir.andromeda.movieado.feature.genre

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.genre.GenreRepository
import ir.andromeda.movieado.data.movie.Movie

class GenreViewModel(
    bundle: Bundle,
    genreRepository: GenreRepository
) :
    MovieadoViewModel() {

    val genresLiveData = MutableLiveData<List<Movie>>()

    init {

        progressBarLiveData.value = true

        genreRepository.getByGenre(bundle.getString(EXTRA_KEY_TITLE)!!)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : MovieadoSingleObserver<List<Movie>>(compositeDisposable) {
                override fun onSuccess(t: List<Movie>) {
                    genresLiveData.value = t
                }
            })    }

}