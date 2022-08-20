package ir.andromeda.cartoonabad.data.episode

import io.reactivex.Completable
import io.reactivex.Single

class EpisodeRepositoryImpl(
    private val localDataSource: EpisodeLocalDataSource,
    private val remoteDataSource: EpisodeRemoteDataSource
) : EpisodeRepository {

    override fun getBookmarkEpisodes(): Single<List<Episode>> =
        localDataSource.getBookmarkEpisodes()

    override fun addToBookmark(episode: Episode): Completable =
        localDataSource.addToBookmark(episode)

    override fun deleteFromBookmark(episode: Episode): Completable =
        localDataSource.deleteFromBookmark(episode)

    override fun getAllEpisodes(seriesId: String): Single<List<Episode>> =
        remoteDataSource.getAllEpisodes(seriesId)

}