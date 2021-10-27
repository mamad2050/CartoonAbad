package ir.andromeda.cartoonabad.data.downloaded

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.DELETE

@Dao
interface DownloadedEpisodeLocalDataSource : DownloadedEpisodeDataSource {

    @Query("SELECT * FROM downloads")
    override fun getDownloadedEpisodes(): Single<List<DownloadedEpisode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToDownloads(episode: DownloadedEpisode): Completable

    @Delete
    override fun deleteFromDownloads(episode: DownloadedEpisode): Completable

}