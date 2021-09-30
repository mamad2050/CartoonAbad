package ir.andromeda.cartoonabad.data.episode

import io.reactivex.Completable
import io.reactivex.Single

interface EpisodeRepository {

    fun getFavoriteEpisodes(): Single<List<Episode>>

    fun addToFavorite(episode: Episode): Completable

    fun deleteFromFavorite(episode: Episode): Completable
}