package ir.andromeda.cartoonabad.data.video

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.AppData
import ir.andromeda.cartoonabad.services.http.ApiService

class VideoRemoteDataSource(private val apiService: ApiService) : VideoDataSource {

    override fun getAnimations(): Single<List<Video>> = apiService.getSeries()
    override fun getVersion(): Single<AppData> = apiService.getAppVersion()


}