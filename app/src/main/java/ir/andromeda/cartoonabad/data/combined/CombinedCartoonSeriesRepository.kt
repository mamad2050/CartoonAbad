package ir.andromeda.cartoonabad.data.combined

import io.reactivex.Single

interface CombinedCartoonSeriesRepository {

    fun getSearchResult(word: String): Single<List<CombinedCartoonSeries>>
}