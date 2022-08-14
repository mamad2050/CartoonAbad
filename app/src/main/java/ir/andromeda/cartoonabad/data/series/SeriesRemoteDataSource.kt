package ir.andromeda.cartoonabad.data.series

import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class SeriesRemoteDataSource(private val apiService: ApiService) : SeriesDataSource {
    override fun getSeries(sort: String, page: Int): Single<List<Series>> =
        apiService.getSeries(sort,page)

    override fun getSeriesByGenre(genre_id: String): Single<List<Series>> =
        apiService.getSeriesByGenre(genre_id)

    override fun getSeriesDetail(series_id: String): Single<Series> =
        apiService.getSeriesDetail(series_id)
}