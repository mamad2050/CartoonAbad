package ir.andromeda.cartoonabad.data.downloaded

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DownloadedLocalDataSource : DownloadedDataSource {

    @Query("SELECT * FROM downloads")
    override fun getDownloadedEpisodes(): Single<List<Downloaded>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToDownloads(episode: Downloaded): Completable

    @Delete
    override fun deleteFromDownloads(episode: Downloaded): Completable

}