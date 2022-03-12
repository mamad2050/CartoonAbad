package ir.andromeda.cartoonabad.data.series

import io.reactivex.Single

interface SeriesDataSource {
    fun getSeries(path: String): Single<List<Series>>
}