package ir.andromeda.cartoonabad.data.episode

import io.reactivex.Completable
import io.reactivex.Single

interface EpisodeDataSource {

    fun getFavoriteEpisodes(): Single<List<Episode>>

    fun addToFavorites(episode: Episode): Completable

    fun deleteFromFavorites(episode: Episode): Completable
}