package ir.andromeda.cartoonabad.data.message

import com.google.gson.JsonObject
import io.reactivex.Single

class MessageRepositoryImpl(
    private val remoteDataSource: MessageRemoteDataSource
) : MessageRepository {
    override fun sendMessage(jsonObject: JsonObject): Single<MessageResponse> {
        return remoteDataSource.sendMessage(jsonObject)
    }
}