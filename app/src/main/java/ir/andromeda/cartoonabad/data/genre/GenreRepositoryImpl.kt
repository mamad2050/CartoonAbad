package ir.andromeda.cartoonabad.data.genre

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries


class GenreRepositoryImpl(private val remoteDataSource: GenreRemoteDataSource) : GenreRepository {
    override fun getGenres(): Single<List<Genre>> = remoteDataSource.getGenres()

    override fun getByGenre(title: String): Single<List<CombinedCartoonSeries>> =
        remoteDataSource.getByGenre(title)
}
