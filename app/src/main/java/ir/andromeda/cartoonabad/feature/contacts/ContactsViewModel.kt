package ir.andromeda.cartoonabad.feature.contacts

import ir.andromeda.cartoonabad.common.CartoonAbadCompletableObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.message.MessageRepository

class ContactsViewModel(private val messageRepository: MessageRepository) : CartoonAbadViewModel() {

    fun addMessage(topic: String, message: String, email: String): Boolean {
        var result = false
        messageRepository.addMessage(topic, message, email)
            .doFinally { progressBarLiveData.postValue(false) }
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    result = true
                }
            })
        return result
    }
}