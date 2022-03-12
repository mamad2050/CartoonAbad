package ir.andromeda.cartoonabad.data.series

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Series(
    val description: String,
    val genre_id: Int,
    val id: Int,
    val image: String,
    val name: String,
    val rate: String,
    val views: Int
) : Parcelable