package ir.andromeda.cartoonabad.data.cartoon

import io.reactivex.Single

interface CartoonRepository {
    fun getCartoons(path: String): Single<List<Cartoon>>
}