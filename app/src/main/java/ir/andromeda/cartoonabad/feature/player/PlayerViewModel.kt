package ir.andromeda.cartoonabad.feature.player

import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel

class PlayerViewModel : CartoonAbadViewModel() {

    val adResponseViewModel = MutableLiveData<String>()

    init {

    }

}