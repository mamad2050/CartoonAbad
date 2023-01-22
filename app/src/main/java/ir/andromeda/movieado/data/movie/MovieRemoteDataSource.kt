package ir.andromeda.movieado.data.movie

import io.reactivex.Single
import ir.andromeda.movieado.services.http.ApiService

class MovieRemoteDataSource(private val apiService: ApiService) : MovieDataSource {

    override fun getMovies(sort: String, type: String, page: Int): Single<List<Movie>> =
        apiService.getMovies(sort, type, page)

    override fun getMovieDetail(movieId: String): Single<Movie> =
        apiService.getMovieDetail(movieId)

    override fun getMoviesByGenre(genreId: String): Single<List<Movie>> =
        apiService.getMoviesByGenre(genreId)

    override fun searchMovie(query: String): Single<List<Movie>> =
        apiService.searchMovie(query)
}