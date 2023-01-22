package ir.andromeda.movieado.feature.player

import androidx.lifecycle.MutableLiveData
import ir.andromeda.movieado.common.MovieadoViewModel

class PlayerViewModel : MovieadoViewModel() {

    val adResponseViewModel = MutableLiveData<String>()

    init {

    }

}