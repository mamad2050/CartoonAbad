package ir.andromeda.cartoonabad.feature.home

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.banner.Banner
import ir.andromeda.cartoonabad.data.banner.BannerRepository
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.data.cartoon.CartoonRepository
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.data.genre.GenreRepository
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.data.series.SeriesRepository

class HomeViewModel(
    genreRepository: GenreRepository,
    seriesRepository: SeriesRepository,
    cartoonRepository: CartoonRepository,
    bannerRepository: BannerRepository
) : CartoonAbadViewModel() {

    val genresLiveData = MutableLiveData<List<Genre>>()
    val latestSeriesLiveData = MutableLiveData<List<Series>>()
    val latestCartoonsLiveData = MutableLiveData<List<Cartoon>>()
    val popularSeriesLiveData = MutableLiveData<List<Series>>()
    val popularCartoonsLiveData = MutableLiveData<List<Cartoon>>()
    val bannersLiveData = MutableLiveData<List<Banner>>()

    init {
        progressBarLiveData.value = true


        bannerRepository.getBanners()
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    bannersLiveData.value = t
                }

            })

        genreRepository.getGenres()
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Genre>>(compositeDisposable) {
                override fun onSuccess(t: List<Genre>) {
                    genresLiveData.value = t
                }
            })

        seriesRepository.getSeries(SORT_BY_LATEST)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : CartoonAbadSingleObserver<List<Series>>(compositeDisposable) {
                override fun onSuccess(t: List<Series>) {
                    latestSeriesLiveData.value = t
                }
            })

        cartoonRepository.getCartoons(SORT_BY_LATEST)
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Cartoon>>(compositeDisposable) {
                override fun onSuccess(t: List<Cartoon>) {
                    latestCartoonsLiveData.value = t
                }
            })

        seriesRepository.getSeries(SORT_BY_VIEW)
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Series>>(compositeDisposable) {
                override fun onSuccess(t: List<Series>) {
                    popularSeriesLiveData.value = t
                }
            })

        cartoonRepository.getCartoons(SORT_BY_VIEW)
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Cartoon>>(compositeDisposable) {
                override fun onSuccess(t: List<Cartoon>) {
                    popularCartoonsLiveData.value = t
                }
            })
    }

//    fun showAnimationList() {
//        progressBarLiveData.value = true
//
//        animationRepository.getAnimations()
//            .doFinally { progressBarLiveData.postValue(false) }
//            .asyncNetworkRequest()
//            .subscribe(object : CartoonAbadSingleObserver<List<Animation>>(compositeDisposable) {
//                override fun onSuccess(t: List<Animation>) {
//                    animationsLiveData.value = t
//                    Timber.i(t.toString())
//                }
//            })
//    }

//    fun getVersionNumber(): Single<AppData> {
//        return animationRepository.getVersion()
//    }


}