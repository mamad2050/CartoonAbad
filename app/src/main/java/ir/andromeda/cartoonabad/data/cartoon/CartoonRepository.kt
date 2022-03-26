package ir.andromeda.cartoonabad.data.cartoon

import io.reactivex.Single

interface CartoonRepository {
    fun getCartoons(sort: String): Single<List<Cartoon>>

    fun getCartoonDetail(cartoon_id:String):Single<Cartoon>

}