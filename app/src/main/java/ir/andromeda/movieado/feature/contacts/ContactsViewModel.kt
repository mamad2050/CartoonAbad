package ir.andromeda.movieado.feature.contacts

import com.google.gson.JsonObject
import io.reactivex.Single
import ir.andromeda.movieado.common.MovieadoViewModel
import ir.andromeda.movieado.data.message.MessageRepository
import ir.andromeda.movieado.data.message.MessageResponse

class ContactsViewModel(private val messageRepository: MessageRepository) : MovieadoViewModel() {

    fun sendMessage(jsonObject: JsonObject): Single<MessageResponse> {
        progressBarLiveData.value = true
        return messageRepository.sendMessage(jsonObject)
            .doFinally {
                progressBarLiveData.postValue(false)
            }

    }
}