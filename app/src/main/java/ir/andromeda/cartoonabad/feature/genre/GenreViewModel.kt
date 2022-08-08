package ir.andromeda.cartoonabad.feature.genre

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.data.genre.GenreRepository
import ir.andromeda.cartoonabad.data.subscription.Subscription

class GenreViewModel(private val genreRepository: GenreRepository) :
    CartoonAbadViewModel() {

    val genresLiveData = MutableLiveData<List<Genre>>()

    init {
        showGenres()
    }

    fun showGenres() {

        progressBarLiveData.value = true

        genreRepository.getGenres()
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : CartoonAbadSingleObserver<List<Genre>>(compositeDisposable) {
                override fun onSuccess(t: List<Genre>) {
                    genresLiveData.value = t
                }
            })
    }

}