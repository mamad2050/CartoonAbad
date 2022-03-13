package ir.andromeda.cartoonabad.data.series

import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class SeriesRemoteDataSource(private val apiService: ApiService) : SeriesDataSource {
    override fun getSeries(sort: String): Single<List<Series>> = apiService.getSeries(sort)
}