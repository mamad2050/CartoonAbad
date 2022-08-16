package ir.andromeda.cartoonabad.data.combined

import io.reactivex.Single

interface CombinedCartoonSeriesDataSource {
    fun getSearchResult(word: String) : Single<List<CombinedCartoonSeries>>
}