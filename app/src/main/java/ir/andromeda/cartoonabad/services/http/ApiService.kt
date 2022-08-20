package ir.andromeda.cartoonabad.services.http

import com.google.gson.JsonObject
import io.reactivex.Single
import ir.andromeda.cartoonabad.data.AppData
import ir.andromeda.cartoonabad.data.banner.Banner
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.data.message.MessageResponse
import ir.andromeda.cartoonabad.data.season.Season
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.data.subscription.Subscription
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("series/list")
    fun getSeries(
        @Query("sort") sort: String,
        @Query("page") page: Int = 1
    ): Single<List<Series>>

    @GET("cartoons/list")
    fun getCartoons(
        @Query("sort") sort: String,
        @Query("page") page: Int = 1
    ): Single<List<Cartoon>>

    @GET("series/{id}/seasons")
    fun getSeasons(@Path("id") seriesId: String): Single<List<Season>>

    @GET("series/{id}/episodes")
    fun getEpisodes(@Path("id") seriesId: String): Single<List<Episode>>

    @GET("cartoons/{id}")
    fun getCartoonDetail(@Path("id") cartoonId: String): Single<Cartoon>

    @GET("series/{id}")
    fun getSeriesDetail(@Path("id") seriesId: String): Single<Series>

    @GET("series/list")
    fun getSeriesByGenre(@Query("genre_id") genreId: String): Single<List<Series>>

    @GET("banners/")
    fun getBanners(): Single<List<Banner>>

    @POST("suggestions")
    fun sendMessage(
        @Body jsonObject: JsonObject
    ): Single<MessageResponse>

    @GET("subscriptions")
    fun getSubscriptions(): Single<List<Subscription>>

    @GET("genres/list")
    fun getGenres(): Single<List<Genre>>

    @GET("genres/{title}")
    fun getByGenre(@Path("title")title : String): Single<List<CombinedCartoonSeries>>

    @GET("search/{query}")
    fun getSearchResult(@Path("query") word: String): Single<List<CombinedCartoonSeries>>

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