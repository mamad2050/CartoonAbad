package ir.andromeda.cartoonabad.data.genre

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries

interface GenreRepository {

    fun getGenres(): Single<List<Genre>>

    fun getByGenre(title : String) : Single<List<CombinedCartoonSeries>>


}