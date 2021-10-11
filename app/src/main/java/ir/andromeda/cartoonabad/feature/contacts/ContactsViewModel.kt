package ir.andromeda.cartoonabad.feature.contacts

import io.reactivex.Single
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.data.message.MessageRepository
import ir.andromeda.cartoonabad.data.message.MessageResponse

class ContactsViewModel(private val messageRepository: MessageRepository) : CartoonAbadViewModel() {

    fun sendMessage(topic: String, message: String, email: String): Single<MessageResponse> {
        progressBarLiveData.value = true
        return messageRepository.sendMessage(topic, message, email)
            .doFinally {
                progressBarLiveData.postValue(false)
            }

    }
}