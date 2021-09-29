package ir.andromeda.cartoonabad.services.http


import io.reactivex.Single
import ir.andromeda.cartoonabad.data.animation.Animation
import ir.andromeda.cartoonabad.data.season.Season
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("getAnimations.php")
    fun getAnimations(): Single<List<Animation>>

    @GET("getSeasons.php")
    fun getSeasons(@Query("animation_id") animationId: Int): Single<List<Season>>

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