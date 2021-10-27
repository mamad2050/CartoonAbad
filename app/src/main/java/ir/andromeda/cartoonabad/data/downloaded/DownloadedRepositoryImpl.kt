package ir.andromeda.cartoonabad.data.downloaded

import io.reactivex.Completable
import io.reactivex.Single

class DownloadedRepositoryImpl(
    private val localDataSource: DownloadedLocalDataSource
) : DownloadedRepository {

    override fun getDownloadedEpisodes(): Single<List<Downloaded>>
    = localDataSource.getDownloadedEpisodes()

    override fun addToDownloads(episode: Downloaded): Completable
    = localDataSource.addToDownloads(episode)

    override fun deleteFromDownloads(episode: Downloaded): Completable
    = localDataSource.deleteFromDownloads(episode)

}