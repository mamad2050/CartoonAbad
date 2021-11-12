package ir.andromeda.cartoonabad.data.animation

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.AppData

interface AnimationRepository {

    fun getAnimations(): Single<List<Animation>>

    fun getVersion(): Single<AppData>

}