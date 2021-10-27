package ir.andromeda.cartoonabad.data.downloaded

import io.reactivex.Completable
import io.reactivex.Single

interface DownloadedDataSource {

    fun getDownloadedEpisodes(): Single<List<Downloaded>>

    fun addToDownloads(episode: Downloaded): Completable

    fun deleteFromDownloads(episode: Downloaded): Completable
}