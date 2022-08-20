package ir.andromeda.cartoonabad.feature.genre

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.google.gson.internal.bind.DateTypeAdapter
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.data.genre.GenreRepository
import ir.andromeda.cartoonabad.data.subscription.Subscription

class GenreViewModel(
    bundle: Bundle,
    genreRepository: GenreRepository
) :
    CartoonAbadViewModel() {

    val genresLiveData = MutableLiveData<List<CombinedCartoonSeries>>()

    init {

        progressBarLiveData.value = true

        genreRepository.getByGenre(bundle.getString(EXTRA_KEY_TITLE)!!)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : CartoonAbadSingleObserver<List<CombinedCartoonSeries>>(compositeDisposable) {
                override fun onSuccess(t: List<CombinedCartoonSeries>) {
                    genresLiveData.value = t
                }
            })    }

}