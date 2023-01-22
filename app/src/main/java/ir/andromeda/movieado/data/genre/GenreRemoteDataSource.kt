package ir.andromeda.movieado.data.genre

import io.reactivex.Single
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.services.http.ApiService

class GenreRemoteDataSource(private val apiService: ApiService) : GenreDataSource {

    override fun getGenres(): Single<List<Genre>> = apiService.getGenres()

    override fun getByGenre(title: String): Single<List<Movie>> =
        apiService.getByGenre(title)

}