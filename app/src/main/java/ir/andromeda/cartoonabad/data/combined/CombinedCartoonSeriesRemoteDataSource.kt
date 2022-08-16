package ir.andromeda.cartoonabad.data.combined

import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class CombinedCartoonSeriesRemoteDataSource(val apiService: ApiService) :
    CombinedCartoonSeriesDataSource {
    override fun getSearchResult(word: String): Single<List<CombinedCartoonSeries>> =
        apiService.getSearchResult(word)
}