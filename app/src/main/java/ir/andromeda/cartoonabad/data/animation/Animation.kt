package ir.andromeda.cartoonabad.data.animation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Animation(
    val id: String,
    val image: String,
    val name: String,
    val no_episodes: String,
    val no_seasons: String,
    val rate: String
) : Parcelable