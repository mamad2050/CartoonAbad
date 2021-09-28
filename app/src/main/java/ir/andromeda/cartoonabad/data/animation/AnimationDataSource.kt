package ir.andromeda.cartoonabad.data.animation

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.Episode
import ir.andromeda.cartoonabad.data.Season

interface AnimationDataSource {

    fun getAnimations(): Single<List<Animation>>

//    fun getSeasons(animationId: Int): Single<List<Season>>
//
//    fun getEpisodes(seasonId: Int): Single<List<Episode>>
//
//    fun addToFavorite(episode: Episode)
//
//    fun download(episode: Episode)
}