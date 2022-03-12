package ir.andromeda.cartoonabad.services.http

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.AppData
import ir.andromeda.cartoonabad.data.animation.Animation
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.data.message.MessageResponse
import ir.andromeda.cartoonabad.data.season.Season
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.data.subscription.Subscription
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("getSeries.php")
    fun getSeries1(): Single<List<Animation>>

    @GET("series/{path}")
    fun getSeries(@Path("path") path: String): Single<List<Series>>

    @GET("cartoons/{path}")
    fun getCartoons(@Path("path") path: String): Single<List<Cartoon>>

    @GET("getSeasons.php")
    fun getSeasons(@Query("animation_id") videoId: Int): Single<List<Season>>

    @FormUrlEncoded
    @POST("sendMessage.php")
    fun sendMessage(
        @Field("title") title: String,
        @Field("message") message: String,
        @Field("email") email: String
    ): Single<MessageResponse>

    @GET("getSubscriptions.php")
    fun getSubscriptions(): Single<List<Subscription>>

    @GET("getAppVersion.php")
    fun getAppVersion(): Single<AppData>

}

fun createApiServiceInstance(): ApiService {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://cartoon-abad.ir/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)
}