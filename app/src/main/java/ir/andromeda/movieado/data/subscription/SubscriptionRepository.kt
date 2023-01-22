package ir.andromeda.movieado.data.subscription

import io.reactivex.Single

interface SubscriptionRepository {

    fun getSubscriptions():Single<List<Subscription>>

}