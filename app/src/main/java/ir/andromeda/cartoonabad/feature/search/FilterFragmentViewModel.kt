package ir.andromeda.cartoonabad.feature.search

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.data.genre.GenreRepository

class FilterFragmentViewModel(
    genreRepository: GenreRepository
) : CartoonAbadViewModel() {

    val genresLiveData = MutableLiveData<List<Genre>>()

    init {
        genreRepository.getGenres()
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Genre>>(compositeDisposable) {
                override fun onSuccess(t: List<Genre>) {
                    genresLiveData.value = t
                }
            })
    }
}