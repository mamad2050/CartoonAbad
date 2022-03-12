package ir.andromeda.cartoonabad.data.animation

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.AppData
import ir.andromeda.cartoonabad.services.http.ApiService

class AnimationRemoteDataSource(private val apiService: ApiService) : AnimationDataSource {

    override fun getAnimations(): Single<List<Animation>> = apiService.getSeries1()
    override fun getVersion(): Single<AppData> = apiService.getAppVersion()


}