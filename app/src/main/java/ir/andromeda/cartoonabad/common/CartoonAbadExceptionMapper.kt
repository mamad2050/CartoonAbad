package ir.andromeda.cartoonabad.common

import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.PurchaseException
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

class CartoonAbadExceptionMapper {
    companion object {
        fun map(throwable: Throwable): CartoonAbadEvent {
            if (throwable is HttpException) {
                try {
                    val errorJsonObject = JSONObject(throwable.response()?.errorBody()!!.string())
                    val errorMessage = errorJsonObject.getString("message")
                    return CartoonAbadEvent(
                        CartoonAbadEvent.Type.SIMPLE,
                        stringMessage = errorMessage
                    )
                } catch (exception: Exception) {
                    Timber.e(exception)
                }
            } else if (throwable is PurchaseException) {
                return CartoonAbadEvent(CartoonAbadEvent.Type.PURCHASE, stringMessage = "")
            }

            return CartoonAbadEvent(
                CartoonAbadEvent.Type.SIMPLE,
                resMessage = R.string.unknown_error
            )
        }
    }
}