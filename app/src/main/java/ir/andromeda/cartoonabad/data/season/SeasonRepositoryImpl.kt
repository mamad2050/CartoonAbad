package ir.andromeda.cartoonabad.data.season

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.episode.EpisodeLocalDataSource

class SeasonRepositoryImpl(
    private val remoteDataSource: SeasonRemoteDataSource,
) : SeasonRepository {

    override fun getSeasons(series_id: String): Single<List<Season>> =
        remoteDataSource.getSeasons(series_id)

}