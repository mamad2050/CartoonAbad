package ir.andromeda.movieado.data.subscription

import com.google.gson.annotations.SerializedName

data class Subscription(
    @SerializedName("_id") val id: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("price") val price: Int
)