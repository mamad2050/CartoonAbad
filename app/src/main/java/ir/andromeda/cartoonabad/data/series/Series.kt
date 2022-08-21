package ir.andromeda.cartoonabad.data.series

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Series(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("banner") val banner: String,
    @SerializedName("description") val description: String,
    @SerializedName("rate") val rate: String,
    @SerializedName("views") val views: Int,
    @SerializedName("genres") val genres: List<String>
) : Parcelable
