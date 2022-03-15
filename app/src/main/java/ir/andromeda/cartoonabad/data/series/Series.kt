package ir.andromeda.cartoonabad.data.series

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Series(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val rate: String,
    val genre_id: Int,
    val views: Int
) : Parcelable

const val SORT_BY_ID = "id"
const val SORT_BY_LATEST = "latest"
const val SORT_BY_VIEW = "view"