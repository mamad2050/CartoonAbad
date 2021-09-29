package ir.andromeda.cartoonabad.data.season

import com.google.gson.annotations.SerializedName
import ir.andromeda.cartoonabad.data.Episode

data class Season(
    val id: String,
    val image: String,
    val name: String,
    val animation_id: String,
    @SerializedName("episodes")
    val episodeList: List<Episode>
)