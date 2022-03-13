package ir.andromeda.cartoonabad.data.genre

import io.reactivex.Single

interface GenreRepository {

    fun getGenres(): Single<List<Genre>>

}