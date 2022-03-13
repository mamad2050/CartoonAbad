package ir.andromeda.cartoonabad.data.series

import io.reactivex.Single

interface SeriesDataSource {
    fun getSeries(sort: String): Single<List<Series>>
}