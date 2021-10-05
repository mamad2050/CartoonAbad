package ir.andromeda.cartoonabad.data.message

import io.reactivex.Completable

interface MessageRepository {
    fun addMessage(topic: String, message: String, email: String): Completable
}