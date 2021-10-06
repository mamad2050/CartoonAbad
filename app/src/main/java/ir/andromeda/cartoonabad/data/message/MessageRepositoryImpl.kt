package ir.andromeda.cartoonabad.data.message

import io.reactivex.Completable
import io.reactivex.Single

class MessageRepositoryImpl(
    private val remoteDataSource: MessageRemoteDataSource
) : MessageRepository {

    override fun addMessage(topic: String, message: String, email: String): Single<Unit> {
        return remoteDataSource.addMessage(topic, message, email)
    }
}