package ir.andromeda.cartoonabad.data.download

import io.reactivex.Completable
import io.reactivex.Single

interface DownloadedRepository {

    fun getDownloadedEpisodes(): Single<List<Downloaded>>

    fun addToDownloads(episode: Downloaded): Completable

    fun deleteFromDownloads(episode: Downloaded): Completable
}