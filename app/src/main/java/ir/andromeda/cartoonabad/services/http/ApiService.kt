package ir.andromeda.cartoonabad.services.http


import io.reactivex.Single
import ir.andromeda.cartoonabad.data.animation.Animation
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {

    @GET("getAnimations.php")
    fun getAnimations(): Single<List<Animation>>

}

fun createApiServiceInstance(): ApiService {

    val okHttpClient = OkHttpClient.Builder().build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://cartoon-abad.ir/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)
}