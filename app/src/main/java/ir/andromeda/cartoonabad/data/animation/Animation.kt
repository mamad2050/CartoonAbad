package ir.andromeda.cartoonabad.data.animation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Animation(
    val id: String,
    val image: String,
    val name: String
) : Parcelable