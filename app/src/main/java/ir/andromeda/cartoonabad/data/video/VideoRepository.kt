package ir.andromeda.cartoonabad.data.video

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.AppData

interface VideoRepository {

    fun getAnimations(): Single<List<Video>>

    fun getVersion(): Single<AppData>

}