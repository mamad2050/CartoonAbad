package ir.andromeda.movieado.data.banner

import io.reactivex.Single
import ir.andromeda.movieado.services.http.ApiService

class BannerRemoteDataSource(private val apiService: ApiService) : BannerDataSource {

    override fun getBanners(): Single<List<Banner>> = apiService.getBanners()

}