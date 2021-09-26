package ir.andromeda.cartoonabad.data.animation

import io.reactivex.Single

interface AnimationDataSource {

    fun getAnimations(): Single<Animation>

    fun getSeasons(animationId: Int)

    fun getEpisodes(seasonId: Int)

    fun addToFavorite()

    fun download()
}