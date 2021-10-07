package ir.andromeda.cartoonabad.data.message

import com.google.gson.JsonObject
import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class MessageRemoteDataSource(private val apiService: ApiService) : MessageDataSource {

    override fun sendMessage(
        title: String,
        message: String,
        email: String
    ): Single<MessageResponse> {

        return apiService.sendMessage(title, message, email)
    }
}