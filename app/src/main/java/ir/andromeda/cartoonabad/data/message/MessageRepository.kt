package ir.andromeda.cartoonabad.data.message

import com.google.gson.JsonObject
import io.reactivex.Single

interface MessageRepository {
    fun sendMessage(jsonObject: JsonObject): Single<MessageResponse>
}