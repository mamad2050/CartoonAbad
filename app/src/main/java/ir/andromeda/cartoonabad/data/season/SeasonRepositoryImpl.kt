package ir.andromeda.cartoonabad.data.season

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.episode.EpisodeLocalDataSource

class SeasonRepositoryImpl(
    private val remoteDataSource: SeasonRemoteDataSource,
    private val localDataSource: EpisodeLocalDataSource,

) : SeasonRepository {

    override fun getSeasons(animationId: Int): Single<List<Season>> =
        localDataSource.getFavoriteEpisodes()
            .flatMap { favoriteEpisodes ->
                val favoriteEpisodeId = favoriteEpisodes.map { it.id }
                remoteDataSource.getSeasons(animationId).doOnSuccess { seasons ->
                    seasons.forEach { season ->
                        season.episodeList.forEach { episode ->
                            if (favoriteEpisodeId.contains(episode.id)) {
                                episode.isFavorite = true
                            }
                        }
                    }
                }
            }
}