package ir.andromeda.cartoonabad.data.animation

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.AppData

interface AnimationDataSource {
    fun getAnimations(): Single<List<Animation>>
    fun getVersion(): Single<AppData>
}