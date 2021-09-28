package ir.andromeda.cartoonabad.data.animation

import io.reactivex.Single

interface AnimationRepository {

    fun getProducts(): Single<List<Animation>>

}