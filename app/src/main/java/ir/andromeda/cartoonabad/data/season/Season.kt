package ir.andromeda.cartoonabad.data.season

import com.google.gson.annotations.SerializedName
import ir.andromeda.cartoonabad.data.episode.Episode

data class Season(
    val id: String,
    val image: String,
    val name: String,
    val video_id: String,
    @SerializedName("episodes")
    val episodeList: List<Episode>
) {
    //for expand
    var visibility: Boolean = false
}