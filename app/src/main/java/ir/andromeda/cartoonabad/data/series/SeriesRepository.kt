package ir.andromeda.cartoonabad.data.series

import io.reactivex.Single

interface SeriesRepository {
    fun getSeries(path: String): Single<List<Series>>
}