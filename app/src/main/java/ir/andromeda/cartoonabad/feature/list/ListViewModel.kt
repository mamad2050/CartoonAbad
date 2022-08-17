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
    private val seriesRepository: SeriesRepository,
    private val cartoonRepository: CartoonRepository
) : CartoonAbadViewModel() {

    val seriesLiveData = MutableLiveData<List<Series>>()
    val cartoonLiveData = MutableLiveData<List<Cartoon>>()

    init {

        progressBarLiveData.value = true

        when(bundle.getString(MODE)){
            LATEST_SERIES -> getLatestSeries(1)
            LATEST_CARTOONS -> getLatestCartoons(1)
            MOST_VIEWED_SERIES -> getMostViewedSeries(1)
            MOST_VIEWED_CARTOONS -> getMostViewedCartoons(1)
        }

    }

    fun getMostViewedCartoons(page: Int) {

        cartoonRepository.getCartoons(SORT_BY_VIEW, page)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object :
                CartoonAbadSingleObserver<List<Cartoon>>(compositeDisposable) {
                override fun onSuccess(t: List<Cartoon>) {
                    cartoonLiveData.value = t
                }
            })
    }

    fun getMostViewedSeries(page: Int) {

        seriesRepository.getSeries(SORT_BY_VIEW, page)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object :
                CartoonAbadSingleObserver<List<Series>>(compositeDisposable) {
                override fun onSuccess(t: List<Series>) {
                    seriesLiveData.value = t
                }
            })
    }

    fun getLatestCartoons(page: Int) {

        cartoonRepository.getCartoons(SORT_BY_LATEST, page)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object :
                CartoonAbadSingleObserver<List<Cartoon>>(compositeDisposable) {
                override fun onSuccess(t: List<Cartoon>) {
                    cartoonLiveData.value = t
                }
            })
    }

    fun getLatestSeries(page: Int) {

        seriesRepository.getSeries(SORT_BY_LATEST, page)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object :
                CartoonAbadSingleObserver<List<Series>>(compositeDisposable) {
                override fun onSuccess(t: List<Series>) {
                    seriesLiveData.value = t
                }
            })
    }

}