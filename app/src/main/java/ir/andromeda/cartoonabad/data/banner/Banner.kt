package ir.andromeda.cartoonabad.data.banner

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Banner(
    val title:String,
    val image:String
):Parcelable