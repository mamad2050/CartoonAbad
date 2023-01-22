package ir.andromeda.movieado.data.download

import io.reactivex.Completable
import io.reactivex.Single

class DownloadRepositoryImpl(
    private val localDataSource: DownloadLocalDataSource
) : DownloadRepository {

    override fun getDownloadedEpisodes(): Single<List<Download>>
    = localDataSource.getDownloadedEpisodes()

    override fun addToDownloads(episode: Download): Completable
    = localDataSource.addToDownloads(episode)

    override fun deleteFromDownloads(episode: Download): Completable
    = localDataSource.deleteFromDownloads(episode)

}