package ir.andromeda.cartoonabad.data.cartoon

import io.reactivex.Single

interface CartoonDataSource {
    fun getCartoons(sort: String): Single<List<Cartoon>>
}