package ir.andromeda.movieado.data.banner

import io.reactivex.Single

class BannerRepositoryImpl(private val dataSource: BannerRemoteDataSource) : BannerRepository {
    override fun getBanners(): Single<List<Banner>> = dataSource.getBanners()
}