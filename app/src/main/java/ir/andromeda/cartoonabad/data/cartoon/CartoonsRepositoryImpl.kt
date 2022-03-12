package ir.andromeda.cartoonabad.data.cartoon

import io.reactivex.Single

class CartoonsRepositoryImpl(
    private val remoteDataSource: CartoonRemoteDataSource
) : CartoonRepository {
    override fun getCartoons(path: String): Single<List<Cartoon>> =
        remoteDataSource.getCartoons(path)
}