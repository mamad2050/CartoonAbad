package ir.andromeda.cartoonabad.data.message

import io.reactivex.Completable
import io.reactivex.Single

class MessageRepositoryImpl(
    private val remoteDataSource: MessageRemoteDataSource
) : MessageRepository {
    override fun sendMessage(title: String, message: String, email: String): Single<MessageResponse> {
        return remoteDataSource.sendMessage(title, message, email)
    }
}