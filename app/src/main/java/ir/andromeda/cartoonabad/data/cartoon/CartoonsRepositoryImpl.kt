package ir.andromeda.cartoonabad.data.cartoon

import io.reactivex.Single

class CartoonsRepositoryImpl(
    private val remoteDataSource: CartoonRemoteDataSource
) : CartoonRepository {
    override fun getCartoons(sort: String): Single<List<Cartoon>> =
        remoteDataSource.getCartoons(sort)

    override fun getCartoonDetail(cartoon_id: Int): Single<Cartoon> =
        remoteDataSource.getCartoonDetail(cartoon_id)
}