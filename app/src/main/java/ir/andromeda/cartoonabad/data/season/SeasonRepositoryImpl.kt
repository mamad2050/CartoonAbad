package ir.andromeda.cartoonabad.data.season

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.episode.EpisodeLocalDataSource

class SeasonRepositoryImpl(
    private val seasonRemoteDataSource: SeasonRemoteDataSource

) : SeasonRepository {
    override fun getSeasons(animationId: Int): Single<List<Season>> =
        seasonRemoteDataSource.getSeasons(animationId)
}