package ir.andromeda.movieado.data.subscription

import com.google.gson.annotations.SerializedName

data class Subscription(
    @SerializedName("_id")
    val id: String,
    val duration: String,
    val price: Int
)