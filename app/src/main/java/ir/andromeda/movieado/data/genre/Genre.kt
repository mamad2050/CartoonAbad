package ir.andromeda.movieado.data.genre

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String
) : Parcelable
