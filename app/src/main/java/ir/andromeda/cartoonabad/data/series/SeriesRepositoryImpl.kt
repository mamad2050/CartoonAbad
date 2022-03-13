package ir.andromeda.cartoonabad.data.series

import io.reactivex.Single

class SeriesRepositoryImpl(
    private val remoteDataSource: SeriesRemoteDataSource
) : SeriesRepository {
    override fun getSeries(sort: String): Single<List<Series>> = remoteDataSource.getSeries(sort)
}