package ir.andromeda.cartoonabad.data.episode

import io.reactivex.Completable
import io.reactivex.Single

class EpisodeRepositoryImpl(
    private val localDataSource: EpisodeLocalDataSource
) : EpisodeRepository {

    override fun getFavoriteEpisodes(): Single<List<Episode>> =
        localDataSource.getFavoriteEpisodes()

    override fun addToFavorite(episode: Episode): Completable =
        localDataSource.addToFavorites(episode)

    override fun deleteFromFavorite(episode: Episode): Completable =
        localDataSource.deleteFromFavorites(episode)

}