package ir.andromeda.cartoonabad.feature.purchase

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.subscription.Subscription
import ir.andromeda.cartoonabad.data.subscription.SubscriptionRepository

class PurchaseViewModel(private val subscriptionRepository: SubscriptionRepository) :
    CartoonAbadViewModel() {

    val subscriptionsLiveData = MutableLiveData<List<Subscription>>()

    init {
        showSubscriptionPrices()
    }

    fun showSubscriptionPrices() {

        progressBarLiveData.value = true

        subscriptionRepository.getSubscriptions()
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.postValue(false) }
            .subscribe(object : CartoonAbadSingleObserver<List<Subscription>>(compositeDisposable) {
                override fun onSuccess(t: List<Subscription>) {
                    subscriptionsLiveData.value = t
                }
            })
    }

}