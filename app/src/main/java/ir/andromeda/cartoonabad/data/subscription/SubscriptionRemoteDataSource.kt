package ir.andromeda.cartoonabad.data.subscription

import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class SubscriptionRemoteDataSource(private val apiService: ApiService) : SubscriptionDataSource {

    override fun getSubscriptions(): Single<List<Subscription>> = apiService.getSubscriptions()
}