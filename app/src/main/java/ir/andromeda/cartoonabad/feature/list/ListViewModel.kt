package ir.andromeda.cartoonabad.feature.list

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.data.cartoon.CartoonRepository
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.data.series.SeriesRepository

class ListViewModel(
    val bundle: Bundle,
    val seriesRepository: SeriesRepository,
    val cartoonRepository: CartoonRepository
) : CartoonAbadViewModel() {

    val seriesLiveData = MutableLiveData<List<Series>>()
    val cartoonLiveData = MutableLiveData<List<Cartoon>>()

    init {

        val mode = bundle.getString(MODE)
        progressBarLiveData.value = true

        when (mode) {
            LATEST_SERIES -> {
                seriesRepository.getSeries(SORT_BY_LATEST, 1)
                    .asyncNetworkRequest()
                    .doFinally { progressBarLiveData.postValue(false) }
                    .subscribe(object :
                        CartoonAbadSingleObserver<List<Series>>(compositeDisposable) {
                        override fun onSuccess(t: List<Series>) {
                            seriesLiveData.value = t
                        }
                    })
            }

            LATEST_CARTOONS -> {
                cartoonRepository.getCartoons(SORT_BY_LATEST, 1)
                    .asyncNetworkRequest()
                    .doFinally { progressBarLiveData.postValue(false) }
                    .subscribe(object :
                        CartoonAbadSingleObserver<List<Cartoon>>(compositeDisposable) {
                        override fun onSuccess(t: List<Cartoon>) {
                            cartoonLiveData.value = t
                        }
                    })
            }

            MOST_VIEWED_SERIES -> {
                seriesRepository.getSeries(SORT_BY_VIEW, 1)
                    .asyncNetworkRequest()
                    .doFinally { progressBarLiveData.postValue(false) }
                    .subscribe(object :
                        CartoonAbadSingleObserver<List<Series>>(compositeDisposable) {
                        override fun onSuccess(t: List<Series>) {
                            seriesLiveData.value = t
                        }
                    })
            }

            MOST_VIEWED_CARTOONS -> {
                cartoonRepository.getCartoons(SORT_BY_VIEW, 1)
                    .asyncNetworkRequest()
                    .doFinally { progressBarLiveData.postValue(false) }
                    .subscribe(object :
                        CartoonAbadSingleObserver<List<Cartoon>>(compositeDisposable) {
                        override fun onSuccess(t: List<Cartoon>) {
                            cartoonLiveData.value = t
                        }
                    })
            }
        }
    }


}