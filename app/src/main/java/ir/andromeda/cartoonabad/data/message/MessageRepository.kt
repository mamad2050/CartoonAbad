package ir.andromeda.cartoonabad.data.message

import io.reactivex.Completable
import io.reactivex.Single


interface MessageRepository {
    fun sendMessage(title: String, message: String, email: String): Single<MessageResponse>
}