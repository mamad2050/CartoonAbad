package ir.andromeda.cartoonabad.data.series

import io.reactivex.Single

class SeriesRepositoryImpl(
    private val remoteDataSource: SeriesRemoteDataSource
) : SeriesRepository {
    override fun getSeries(sort: String): Single<List<Series>> = remoteDataSource.getSeries(sort)

    override fun getSeriesByGenre(genre_id: String): Single<List<Series>> =
        remoteDataSource.getSeriesByGenre(genre_id)

    override fun getSeriesDetail(series_id: String): Single<Series> = remoteDataSource.getSeriesDetail(series_id)
}
