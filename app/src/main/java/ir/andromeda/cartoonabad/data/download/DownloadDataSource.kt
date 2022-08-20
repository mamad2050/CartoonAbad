package ir.andromeda.cartoonabad.data.download

import io.reactivex.Completable
import io.reactivex.Single

interface DownloadDataSource {

    fun getDownloadedEpisodes(): Single<List<Download>>

    fun addToDownloads(episode: Download): Completable

    fun deleteFromDownloads(episode: Download): Completable
}