package ir.andromeda.movieado.data.message

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("email") val email: String
)