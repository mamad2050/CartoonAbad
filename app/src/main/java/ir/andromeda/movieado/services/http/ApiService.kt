package ir.andromeda.movieado.services.http

import com.google.gson.JsonObject
import io.reactivex.Single
import ir.andromeda.movieado.data.banner.Banner
import ir.andromeda.movieado.data.episode.Episode
import ir.andromeda.movieado.data.genre.Genre
import ir.andromeda.movieado.data.message.MessageResponse
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.data.season.Season
import ir.andromeda.movieado.data.subscription.Subscription
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("movies/list")
    fun getMovies(
        @Query("sort") sort: String,
        @Query("type") type: String,
        @Query("page") page: Int = 1
    ): Single<List<Movie>>

    @GET("movies/{id}")
    fun getMovieDetail(@Path("id") movieId: String): Single<Movie>

    @GET("movie/list")
    fun getMoviesByGenre(@Query("genre_id") genreId: String): Single<List<Movie>>

    @GET("search/{query}")
    fun searchMovie(@Path("query") word: String): Single<List<Movie>>

    @GET("series/{id}/seasons")
    fun getSeasons(@Path("id") seriesId: String): Single<List<Season>>

    @GET("series/{id}/episodes")
    fun getEpisodes(@Path("id") seriesId: String): Single<List<Episode>>

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
    fun getByGenre(@Path("title") title: String): Single<List<Movie>>

}

fun createApiServiceInstance(): ApiService {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://movieado.ir/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)
}