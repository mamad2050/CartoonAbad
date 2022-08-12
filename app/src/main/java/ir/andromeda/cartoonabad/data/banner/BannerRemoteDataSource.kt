package ir.andromeda.cartoonabad.data.banner

import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class BannerRemoteDataSource(private val apiService: ApiService) : BannerDataSource {

    override fun getBanners(): Single<List<Banner>> = apiService.getBanners()

}