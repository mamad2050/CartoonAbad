package ir.andromeda.cartoonabad.data.subscription

import io.reactivex.Single

interface SubscriptionDataSource {

    fun getSubscriptions() : Single<List<Subscription>>
}