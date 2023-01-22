package ir.andromeda.movieado.data.subscription

import io.reactivex.Single

class SubscriptionRepositoryImpl(private val subscriptionDataSource: SubscriptionDataSource) :
    SubscriptionRepository {

    override fun getSubscriptions(): Single<List<Subscription>> = subscriptionDataSource.getSubscriptions()
}