package ir.andromeda.cartoonabad.data.cartoon

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cartoon(
    @SerializedName("_id")
    val id: String,
    val description: String,
    val duration: String,
    val image: String,
    val name: String,
    val rate: Double,
    val url: String,
    val views: Int,
    val genres: List<String>
) : Parcelable