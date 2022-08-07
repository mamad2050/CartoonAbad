package ir.andromeda.cartoonabad.data.genre

import com.google.gson.annotations.SerializedName


data class Genre(
    @SerializedName("_id")
    val id: String,
    val title:String,
    val image:String
)
