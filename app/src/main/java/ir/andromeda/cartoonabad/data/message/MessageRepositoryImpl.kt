package ir.andromeda.cartoonabad.data.message

import io.reactivex.Completable

class MessageRepositoryImpl(
    private val remoteDataSource: MessageRemoteDataSource
) : MessageRepository {
    override fun sendMessage(title: String, message: String, email: String): Completable {
        return remoteDataSource.sendMessage(title, message, email).ignoreElement()
    }
}