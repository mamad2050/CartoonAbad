package ir.andromeda.cartoonabad.data.genre

import io.reactivex.Single


class GenreRepositoryImpl(private val remoteDataSource: GenreRemoteDataSource) : GenreRepository {
    override fun getGenres(): Single<List<Genre>> = remoteDataSource.getGenres()
}