package ir.andromeda.movieado.data.subscription

import io.reactivex.Single
import ir.andromeda.movieado.services.http.ApiService

class SubscriptionRemoteDataSource(private val apiService: ApiService) : SubscriptionDataSource {

    override fun getSubscriptions(): Single<List<Subscription>> = apiService.getSubscriptions()
}