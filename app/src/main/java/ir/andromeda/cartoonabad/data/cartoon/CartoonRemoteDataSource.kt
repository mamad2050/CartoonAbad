package ir.andromeda.cartoonabad.data.cartoon

import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class CartoonRemoteDataSource(private val apiService: ApiService) : CartoonDataSource {

    override fun getCartoons(sort: String,page : Int): Single<List<Cartoon>> = apiService.getCartoons(sort,page)

    override fun getCartoonDetail(cartoon_id: String): Single<Cartoon> =
        apiService.getCartoonDetail(cartoon_id)
}