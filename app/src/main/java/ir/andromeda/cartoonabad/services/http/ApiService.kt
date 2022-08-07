package ir.andromeda.cartoonabad.services.http

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.AppData
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.data.genre.Genre
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

    @GET("series/list")
    fun getSeries(@Query("sort") sort: String): Single<List<Series>>

    @GET("cartoons/list")
    fun getCartoons(@Query("sort") sort: String): Single<List<Cartoon>>

    @GET("series/{id}/seasons")
    fun getSeasons(@Path("id") series_id: Int): Single<List<Season>>

    @GET("cartoons/{id}")
    fun getCartoonDetail(@Path("id")cartoon_id :String):Single<Cartoon>

    @GET("series/{id}")
    fun getSeriesDetail(@Path("id")series_id :String):Single<Series>

    @GET("series/list")
    fun getSeriesByGenre(@Query("genre_id") genre_id: String) :Single<List<Series>>

    @FormUrlEncoded
    @POST("sendMessage.php")
    fun sendMessage(
        @Field("title") title: String,
        @Field("message") message: String,
        @Field("email") email: String
    ): Single<MessageResponse>

    @GET("subscriptions")
    fun getSubscriptions(): Single<List<Subscription>>

    @GET("getAppVersion.php")
    fun getAppVersion(): Single<AppData>


    @GET("genres/list")
    fun getGenres(): Single<List<Genre>>

}

fun createApiServiceInstance(): ApiService {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://cartoon-abad.ir/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)
}