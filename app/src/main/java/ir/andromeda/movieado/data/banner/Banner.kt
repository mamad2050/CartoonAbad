package ir.andromeda.movieado.data.banner

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Banner(
    @SerializedName("_id")
    val id: String,
    val title: String,
    val image: String,
    val type: String,
    val content_id: String,
) : Parcelable