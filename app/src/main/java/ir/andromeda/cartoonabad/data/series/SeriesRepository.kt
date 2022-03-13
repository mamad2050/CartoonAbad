package ir.andromeda.cartoonabad.data.series

import io.reactivex.Single

interface SeriesRepository {
    fun getSeries(sort: String): Single<List<Series>>
}