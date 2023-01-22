package ir.andromeda.movieado.data.genre

import io.reactivex.Single
import ir.andromeda.movieado.data.movie.Movie

interface GenreDataSource {

    fun getGenres(): Single<List<Genre>>

    fun getByGenre(title: String): Single<List<Movie>>
}