package ir.andromeda.cartoonabad.feature.contacts

import com.google.gson.JsonObject
import io.reactivex.Single
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.data.message.MessageRepository
import ir.andromeda.cartoonabad.data.message.MessageResponse

class ContactsViewModel(private val messageRepository: MessageRepository) : CartoonAbadViewModel() {

    fun sendMessage(jsonObject: JsonObject): Single<MessageResponse> {
        progressBarLiveData.value = true
        return messageRepository.sendMessage(jsonObject)
            .doFinally {
                progressBarLiveData.postValue(false)
            }

    }
}