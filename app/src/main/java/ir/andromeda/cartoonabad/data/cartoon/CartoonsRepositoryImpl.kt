package ir.andromeda.cartoonabad.data.cartoon

import io.reactivex.Single

class CartoonsRepositoryImpl(
    private val remoteDataSource: CartoonRemoteDataSource
) : CartoonRepository {
    override fun getCartoons(sort: String, page: Int): Single<List<Cartoon>> =
        remoteDataSource.getCartoons(sort, page)

    override fun getCartoonDetail(cartoon_id: String): Single<Cartoon> =
        remoteDataSource.getCartoonDetail(cartoon_id)
}