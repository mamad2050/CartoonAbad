package ir.andromeda.cartoonabad.data.season

import io.reactivex.Single

class SeasonRepositoryImpl(private val seasonRemoteDataSource: SeasonRemoteDataSource) : SeasonRepository {
    override fun getSeasons(animationId: Int): Single<List<Season>> =
        seasonRemoteDataSource.getSeasons(animationId)

}