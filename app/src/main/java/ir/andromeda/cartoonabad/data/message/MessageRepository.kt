package ir.andromeda.cartoonabad.data.message

import io.reactivex.Completable


interface MessageRepository {
    fun sendMessage(title: String, message: String, email: String): Completable
}