package ir.andromeda.cartoonabad.feature.contacts

import androidx.lifecycle.MediatorLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.message.MessageRepository

class ContactsViewModel(private val messageRepository: MessageRepository) : CartoonAbadViewModel() {

    val showResult = MediatorLiveData<Boolean>()

    fun addMessage(topic: String, message: String, email: String) {
        progressBarLiveData.value = true
        messageRepository.addMessage(topic, message, email)
            .doFinally { progressBarLiveData.postValue(false) }
            .asyncNetworkRequest()
            .subscribe(object : CartoonAbadSingleObserver<Unit>(compositeDisposable) {
                override fun onSuccess(t: Unit) {
                    showResult.value = true
                }

            })
    }
}