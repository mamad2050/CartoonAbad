package ir.andromeda.movieado.data.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
    @SerializedName("banner") val banner: String,
    @SerializedName("type") val type: String,
    @SerializedName("imdb") val imdb: Double,
    @SerializedName("genres") val genres: List<String>,
    @SerializedName("duration") val duration: String?,
    @SerializedName("url") val url: String?,
) : Parcelable
