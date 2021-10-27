package ir.andromeda.cartoonabad.data.downloaded

import io.reactivex.Completable
import io.reactivex.Single

interface DownloadedEpisodeDataSource {

    fun getDownloadedEpisodes(): Single<List<DownloadedEpisode>>

    fun addToDownloads(episode: DownloadedEpisode): Completable

    fun deleteFromDownloads(episode: DownloadedEpisode): Completable
}