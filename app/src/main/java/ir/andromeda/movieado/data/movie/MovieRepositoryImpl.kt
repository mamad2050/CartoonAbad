package ir.andromeda.movieado.data.movie

import io.reactivex.Single

class MovieRepositoryImpl(
    private val remoteDataSource: MovieRemoteDataSource
) : MovieRepository {
    override fun getMovies(sort: String, type: String, page: Int): Single<List<Movie>> =
        remoteDataSource.getMovies(sort, type, page)

    override fun getMovieDetail(movieId: String): Single<Movie> =
        remoteDataSource.getMovieDetail(movieId)

    override fun getMoviesByGenre(genreId: String): Single<List<Movie>> =
        remoteDataSource.getMoviesByGenre(genreId)

    override fun searchMovie(query: String): Single<List<Movie>> =
        remoteDataSource.searchMovie(query)
}