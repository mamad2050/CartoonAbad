package ir.andromeda.movieado.data

import androidx.annotation.StringRes

class MovieadoEvent(
    val type: Type,
    @StringRes val resMessage: Int = 0,
    val stringMessage: String? = null
) {
    enum class Type {
        SIMPLE, PURCHASE
    }
}