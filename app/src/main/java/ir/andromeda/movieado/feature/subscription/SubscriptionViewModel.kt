package ir.andromeda.movieado.feature.subscription

import androidx.lifecycle.MutableLiveData
import ir.andromeda.movieado.common.MovieadoSingleObserver
import ir.andromeda.movieado.common.MovieadoViewModel
import ir.andromeda.movieado.common.asyncNetworkRequest
import ir.andromeda.movieado.data.subscription.Subscription
import ir.andromeda.movieado.data.subscription.SubscriptionRepository

class SubscriptionViewModel(private val subscriptionRepository: SubscriptionRepository) :
    MovieadoViewModel() {

    val subscriptionsLiveData = MutableLiveData<List<Subscription>>()

    init {
        showSubscriptionPrices()
    }

    fun showSubscriptionPrices() {

        progressBarLiveData.value = true

        subscriptionRepository.getSubscriptions()
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : MovieadoSingleObserver<List<Subscription>>(compositeDisposable) {
                override fun onSuccess(t: List<Subscription>) {
                    subscriptionsLiveData.value = t
                }
            })
    }

}