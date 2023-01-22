package ir.andromeda.movieado.data.movie

import io.reactivex.Single

interface MovieDataSource {
    fun getMovies(sort: String, type: String, page: Int): Single<List<Movie>>

    fun getMovieDetail(movieId: String): Single<Movie>

    fun getMoviesByGenre(genreId: String): Single<List<Movie>>

    fun searchMovie(query: String): Single<List<Movie>>
}