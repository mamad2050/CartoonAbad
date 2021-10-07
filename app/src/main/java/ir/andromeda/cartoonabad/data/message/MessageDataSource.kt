package ir.andromeda.cartoonabad.data.message

import io.reactivex.Single

interface MessageDataSource {
    fun sendMessage(title: String, message: String, email: String): Single<MessageResponse>
}