package ir.andromeda.cartoonabad.data.message

import io.reactivex.Single

interface MessageRepository {
    fun addMessage(topic: String, message: String, email: String): Single<Unit>
}