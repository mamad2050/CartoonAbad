package ir.andromeda.cartoonabad.data.season

import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class SeasonRemoteDataSource(private val apiService: ApiService) : SeasonDataSource {

    override fun getSeasons(series_id: String): Single<List<Season>> =
        apiService.getSeasons(series_id)
}