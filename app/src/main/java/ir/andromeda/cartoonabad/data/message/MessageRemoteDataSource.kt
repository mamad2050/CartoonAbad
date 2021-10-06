package ir.andromeda.cartoonabad.data.message

import com.google.gson.JsonObject
import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class MessageRemoteDataSource(private val apiService: ApiService) : MessageDataSource {

    override fun addMessage(topic: String, message: String, email: String): Single<Unit> {

        return apiService.addMessage(JsonObject().apply {
            addProperty("topic", topic)
            addProperty("message", message)
            addProperty("email", email)
        })
    }
}