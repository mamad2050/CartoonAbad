package ir.andromeda.cartoonabad.feature.home

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.AppData
import ir.andromeda.cartoonabad.data.video.Video
import ir.andromeda.cartoonabad.data.video.VideoRepository
import timber.log.Timber

class HomeViewModel(private val videoRepository: VideoRepository) : CartoonAbadViewModel() {

    val animationsLiveData = MutableLiveData<List<Video>>()

    init {
        showAnimationList()
    }

    fun showAnimationList() {
        progressBarLiveData.value = true

        videoRepository.getAnimations()
            .doFinally { progressBarLiveData.postValue(false) }
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Video>>(compositeDisposable) {
                override fun onSuccess(t: List<Video>) {
                    animationsLiveData.value = t
                    Timber.i(t.toString())
                }
            })
    }

    fun getVersionNumber(): Single<AppData> {
        return videoRepository.getVersion()
    }


}