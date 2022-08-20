package ir.andromeda.cartoonabad.data.genre

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries
import ir.andromeda.cartoonabad.services.http.ApiService

class GenreRemoteDataSource(private val apiService: ApiService) : GenreDataSource {

    override fun getGenres(): Single<List<Genre>> = apiService.getGenres()

    override fun getByGenre(title: String): Single<List<CombinedCartoonSeries>> =
        apiService.getByGenre(title)

}