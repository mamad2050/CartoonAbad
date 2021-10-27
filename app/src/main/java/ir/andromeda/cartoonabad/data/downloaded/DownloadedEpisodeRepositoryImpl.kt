package ir.andromeda.cartoonabad.data.downloaded

import io.reactivex.Completable
import io.reactivex.Single

class DownloadedEpisodeRepositoryImpl(
    private val localDataSource: DownloadedEpisodeLocalDataSource
) : DownloadedEpisodeRepository {

    override fun getDownloadedEpisodes(): Single<List<DownloadedEpisode>>
    = localDataSource.getDownloadedEpisodes()

    override fun addToDownloads(episode: DownloadedEpisode): Completable
    = localDataSource.addToDownloads(episode)

    override fun deleteFromDownloads(episode: DownloadedEpisode): Completable
    = localDataSource.deleteFromDownloads(episode)

}