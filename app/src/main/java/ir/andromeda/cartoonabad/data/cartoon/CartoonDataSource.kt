package ir.andromeda.cartoonabad.data.cartoon

import io.reactivex.Single

interface CartoonDataSource {
    fun getCartoons(sort: String ,page : Int): Single<List<Cartoon>>

    fun getCartoonDetail(cartoon_id: String): Single<Cartoon>
}