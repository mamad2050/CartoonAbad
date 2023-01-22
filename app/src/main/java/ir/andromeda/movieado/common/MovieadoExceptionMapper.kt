package ir.andromeda.movieado.common

import ir.andromeda.movieado.R
import ir.andromeda.movieado.data.MovieadoEvent
import ir.andromeda.movieado.data.PurchaseException
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

class MovieadoExceptionMapper {
    companion object {
        fun map(throwable: Throwable): MovieadoEvent {
            if (throwable is HttpException) {
                try {
                    val errorJsonObject = JSONObject(throwable.response()?.errorBody()!!.string())
                    val errorMessage = errorJsonObject.getString("message")
                    return MovieadoEvent(
                        MovieadoEvent.Type.SIMPLE,
                        stringMessage = errorMessage
                    )
                } catch (exception: Exception) {
                    Timber.e(exception)
                }
            } else if (throwable is PurchaseException) {
                return MovieadoEvent(MovieadoEvent.Type.PURCHASE, stringMessage = "")
            }

            return MovieadoEvent(
                MovieadoEvent.Type.SIMPLE,
                resMessage = R.string.unknown_error
            )
        }
    }
}