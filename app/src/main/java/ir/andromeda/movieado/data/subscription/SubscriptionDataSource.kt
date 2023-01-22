package ir.andromeda.movieado.data.subscription

import io.reactivex.Single

interface SubscriptionDataSource {

    fun getSubscriptions() : Single<List<Subscription>>
}