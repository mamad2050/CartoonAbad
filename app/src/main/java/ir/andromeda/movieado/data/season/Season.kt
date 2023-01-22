package ir.andromeda.movieado.data.season

import com.google.gson.annotations.SerializedName

data class Season(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("movie_id") val movieId: String,
) {
    override fun toString(): String {
        return name
    }
}