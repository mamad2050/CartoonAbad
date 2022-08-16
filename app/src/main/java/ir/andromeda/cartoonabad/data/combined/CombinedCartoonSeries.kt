package ir.andromeda.cartoonabad.data.combined

import com.google.gson.annotations.SerializedName

data class CombinedCartoonSeries(
    @SerializedName("_id")
    val id: String,
    val title: String,
    val image: String,
    val rate: String,
    val type: String
)