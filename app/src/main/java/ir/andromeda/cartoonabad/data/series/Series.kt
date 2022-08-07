package ir.andromeda.cartoonabad.data.series

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Series(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val image: String,
    val description: String,
    val rate: String,
    val genre_id: String,
    val views: Int
) : Parcelable
