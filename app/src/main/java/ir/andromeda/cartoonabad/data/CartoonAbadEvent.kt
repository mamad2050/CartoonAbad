package ir.andromeda.cartoonabad.data

import androidx.annotation.StringRes

class CartoonAbadEvent(
    val type: Type,
    @StringRes val resMessage: Int = 0,
    val stringMessage: String? = null
) {
    enum class Type {
        SIMPLE, PURCHASE
    }
}