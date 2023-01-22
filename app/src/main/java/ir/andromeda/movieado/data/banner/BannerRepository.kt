package ir.andromeda.movieado.data.banner

import io.reactivex.Single

interface BannerRepository {
    fun getBanners(): Single<List<Banner>>
}