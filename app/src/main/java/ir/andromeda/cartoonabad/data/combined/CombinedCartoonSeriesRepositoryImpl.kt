package ir.andromeda.cartoonabad.data.combined

import io.reactivex.Single

class CombinedCartoonSeriesRepositoryImpl(private val remoteDataSource: CombinedCartoonSeriesRemoteDataSource) :
    CombinedCartoonSeriesRepository {

    override fun getSearchResult(word: String): Single<List<CombinedCartoonSeries>> =
        remoteDataSource.getSearchResult(word)
}