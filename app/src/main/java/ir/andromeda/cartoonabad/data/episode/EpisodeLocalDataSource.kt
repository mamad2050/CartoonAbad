package ir.andromeda.cartoonabad.data.episode

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.DELETE

@Dao
interface EpisodeLocalDataSource : EpisodeDataSource {

    @Query("SELECT * FROM episodes")
    override fun getFavoriteEpisodes(): Single<List<Episode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToFavorites(episode: Episode): Completable

    @Delete
    override fun deleteFromFavorites(episode: Episode): Completable

}