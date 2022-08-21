package ir.andromeda.cartoonabad.feature.search

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeriesRepository

class SearchViewModel(private val repository: CombinedCartoonSeriesRepository) :
    CartoonAbadViewModel() {

    val searchLiveData = MutableLiveData<List<CombinedCartoonSeries>>()
    val showNotFoundState = MutableLiveData<Boolean>()

    fun search(word: String) {
        progressBarLiveData.value = true
        repository.getSearchResult(word).asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object :
                CartoonAbadSingleObserver<List<CombinedCartoonSeries>>(compositeDisposable) {
                override fun onSuccess(t: List<CombinedCartoonSeries>) {
                    if (t.isNotEmpty()){
                        searchLiveData.value = t
                        showNotFoundState.value = false
                    }
                    else
                        showNotFoundState.value = true
                }
            })
    }

}