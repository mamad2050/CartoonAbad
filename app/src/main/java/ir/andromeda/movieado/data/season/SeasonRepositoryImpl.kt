package ir.andromeda.movieado.data.season

import io.reactivex.Single

class SeasonRepositoryImpl(
    private val remoteDataSource: SeasonRemoteDataSource,
) : SeasonRepository {

    override fun getSeasons(series_id: String): Single<List<Season>> =
        remoteDataSource.getSeasons(series_id)

}