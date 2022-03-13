package ir.andromeda.cartoonabad.data.genre

import io.reactivex.Single

interface GenreDataSource {

    fun getGenres(): Single<List<Genre>>
}