package ir.andromeda.cartoonabad.data.cartoon

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cartoon(
    val description: String,
    val duration: String,
    val genre_id: Int,
    val id: Int,
    val image: String,
    val name: String,
    val rate: Double,
    val url: String,
    val views: Int
) : Parcelable