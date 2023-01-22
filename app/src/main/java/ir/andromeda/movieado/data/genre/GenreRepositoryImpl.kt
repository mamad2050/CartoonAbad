package ir.andromeda.movieado.data.genre

import io.reactivex.Single
import ir.andromeda.movieado.data.movie.Movie


class GenreRepositoryImpl(private val remoteDataSource: GenreRemoteDataSource) : GenreRepository {
    override fun getGenres(): Single<List<Genre>> = remoteDataSource.getGenres()

    override fun getByGenre(title: String): Single<List<Movie>> =
        remoteDataSource.getByGenre(title)
}
