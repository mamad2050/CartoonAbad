package ir.andromeda.cartoonabad.data.genre

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    @SerializedName("_id")
    val id: String,
    val title: String,
    val image: String
) : Parcelable
