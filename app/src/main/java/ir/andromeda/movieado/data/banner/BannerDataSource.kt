package ir.andromeda.movieado.data.banner

import io.reactivex.Single

interface BannerDataSource {
    fun getBanners() : Single<List<Banner>>
}