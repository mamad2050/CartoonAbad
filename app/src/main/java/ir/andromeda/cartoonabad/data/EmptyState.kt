package ir.andromeda.cartoonabad.data

import androidx.annotation.StringRes

data class EmptyState(
    val mustShow: Boolean, @StringRes val messageResId: Int = 0
)
