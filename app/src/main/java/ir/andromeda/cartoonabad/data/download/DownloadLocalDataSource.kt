package ir.andromeda.cartoonabad.data.download

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DownloadLocalDataSource : DownloadDataSource {

    @Query("SELECT * FROM downloads")
    override fun getDownloadedEpisodes(): Single<List<Download>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToDownloads(episode: Download): Completable

    @Delete
    override fun deleteFromDownloads(episode: Download): Completable

}