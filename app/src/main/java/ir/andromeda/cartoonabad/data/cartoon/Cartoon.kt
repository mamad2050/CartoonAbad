package ir.andromeda.cartoonabad.data.cartoon

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cartoon(
    @SerializedName("_id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("image") val image: String,
    @SerializedName("name") val name: String,
    @SerializedName("rate") val rate: Double,
    @SerializedName("url") val url: String,
    @SerializedName("views") val views: Int,
    @SerializedName("genres") val genres: List<String>
) : Parcelable