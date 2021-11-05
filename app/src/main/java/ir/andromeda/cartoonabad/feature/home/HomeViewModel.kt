package ir.andromeda.cartoonabad.feature.home

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.AppData
import ir.andromeda.cartoonabad.data.animation.Animation
import ir.andromeda.cartoonabad.data.animation.AnimationRepository
import timber.log.Timber

class HomeViewModel(private val animationRepository: AnimationRepository) : CartoonAbadViewModel() {

    val animationsLiveData = MutableLiveData<List<Animation>>()

    init {
        showAnimationList()
    }

    fun showAnimationList() {
        progressBarLiveData.value = true

        animationRepository.getAnimations()
            .doFinally { progressBarLiveData.postValue(false) }
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<List<Animation>>(compositeDisposable) {
                override fun onSuccess(t: List<Animation>) {
                    animationsLiveData.value = t
                    Timber.i(t.toString())
                }
            })
    }

    fun getVersionNumber(): Single<AppData> {
        return animationRepository.getVersion()
    }


}