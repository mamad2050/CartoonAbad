package ir.andromeda.cartoonabad.data.subscription

import io.reactivex.Single

interface SubscriptionRepository {

    fun getSubscriptions():Single<List<Subscription>>

}