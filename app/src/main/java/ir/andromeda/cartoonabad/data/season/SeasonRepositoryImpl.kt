package ir.andromeda.cartoonabad.data.season

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.episode.EpisodeLocalDataSource

class SeasonRepositoryImpl(
    private val remoteDataSource: SeasonRemoteDataSource,
    private val localDataSource: EpisodeLocalDataSource

) : SeasonRepository {
    override fun getSeasons(animationId: Int): Single<List<Season>> =

        localDataSource.getFavoriteEpisodes()
            .flatMap { favoriteEpisodes ->
                remoteDataSource.getSeasons(animationId).doOnSuccess { seasons ->
                    seasons.forEach { season ->
                        season.episodeList.forEach { episode ->
                            val favoriteEpisodeId = favoriteEpisodes.map {
                                it.id
                            }
                            season.episodeList.forEach {ep->
                                if (favoriteEpisodeId.contains(ep.id)) {
                                    ep.isFavorite = true
                                }
                            }
                        }

                    }
                }
            }
}