package ir.andromeda.cartoonabad.feature.list

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.season.Season
import ir.andromeda.cartoonabad.data.season.SeasonRepository
import timber.log.Timber

class ListViewModel(animationId: String, seasonRepository: SeasonRepository) :
    CartoonAbadViewModel() {

    val seasonsLiveData = MutableLiveData<List<Season>>()

    init {

        progressBarLiveData.value = true

        seasonRepository.getSeasons(animationId.toInt())
            .doFinally { progressBarLiveData.postValue(false) }
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Season>>(compositeDisposable) {
                override fun onSuccess(t: List<Season>) {
                    Timber.i(t.toString())
                    seasonsLiveData.value = t
                }
            })
    }
}