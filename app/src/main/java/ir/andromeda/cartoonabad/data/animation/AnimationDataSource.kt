package ir.andromeda.cartoonabad.data.animation

import io.reactivex.Single

interface AnimationDataSource {

    fun getAnimations(): Single<List<Animation>>

    fun getVersion(): Single<Int>

}