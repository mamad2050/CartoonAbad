package ir.andromeda.cartoonabad.feature.contacts

import androidx.lifecycle.MediatorLiveData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import ir.andromeda.cartoonabad.common.CartoonAbadCompletableObserver
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.message.MessageRepository
import ir.andromeda.cartoonabad.data.message.MessageResponse

class ContactsViewModel(private val messageRepository: MessageRepository) : CartoonAbadViewModel() {

    val showResult = MediatorLiveData<Boolean>()

    fun sendMessage(topic: String, message: String, email: String): Single<MessageResponse> {
        progressBarLiveData.value = true
        return messageRepository.sendMessage(topic, message, email)
            .doFinally {
                progressBarLiveData.postValue(false)
            }

    }
}