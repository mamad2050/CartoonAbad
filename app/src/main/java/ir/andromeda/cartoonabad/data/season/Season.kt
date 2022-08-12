package ir.andromeda.cartoonabad.data.season

import com.google.gson.annotations.SerializedName
import ir.andromeda.cartoonabad.data.episode.Episode

data class Season(
    @SerializedName("_id")
    val id: String,
    val image: String,
    val name: String,
    val series_id: String,
) {
    override fun toString(): String {
        return name
    }
}