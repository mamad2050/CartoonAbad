package ir.andromeda.movieado.data.banner

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Banner(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String,
    @SerializedName("type") val type: String,
    @SerializedName("movie_id") val movieId: String,
) : Parcelable