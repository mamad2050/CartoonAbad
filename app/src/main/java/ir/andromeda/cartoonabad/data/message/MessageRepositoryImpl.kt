package ir.andromeda.cartoonabad.data.message

import io.reactivex.Completable

class MessageRepositoryImpl(
    private val remoteDataSource: MessageRemoteDataSource
) : MessageRepository {

    override fun addMessage(topic: String, message: String, email: String): Completable {
        return remoteDataSource.addMessage(topic, message, email)
    }
}