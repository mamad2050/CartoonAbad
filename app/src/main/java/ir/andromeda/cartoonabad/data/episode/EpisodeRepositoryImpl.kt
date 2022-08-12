package ir.andromeda.cartoonabad.data.episode

import io.reactivex.Completable
import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class EpisodeRepositoryImpl(
    private val localDataSource: EpisodeLocalDataSource,
    private val remoteDataSource: EpisodeRemoteDataSource
) : EpisodeRepository {

    override fun getFavoriteEpisodes(): Single<List<Episode>> =
        localDataSource.getFavoriteEpisodes()

    override fun addToFavorite(episode: Episode): Completable =
        localDataSource.addToFavorites(episode)

    override fun deleteFromFavorite(episode: Episode): Completable =
        localDataSource.deleteFromFavorites(episode)

    override fun getAllEpisodes(seriesId: String): Single<List<Episode>> =
        remoteDataSource.getAllEpisodes(seriesId)

}