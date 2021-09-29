package ir.andromeda.cartoonabad.data

data class Season(
    val id: String,
    val image: String,
    val name: String,
    val animation_id: String,
    val episodeList: List<Episode>
)