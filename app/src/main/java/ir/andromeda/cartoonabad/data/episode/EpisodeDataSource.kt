package ir.andromeda.cartoonabad.data.episode

import io.reactivex.Completable
import io.reactivex.Single

interface EpisodeDataSource {

    fun getBookmarkEpisodes(): Single<List<Episode>>

    fun addToBookmark(episode: Episode): Completable

    fun deleteFromBookmark(episode: Episode): Completable

    fun getAllEpisodes(seriesId: String): Single<List<Episode>>
}