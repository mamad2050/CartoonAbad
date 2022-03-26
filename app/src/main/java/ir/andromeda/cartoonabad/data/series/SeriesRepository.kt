package ir.andromeda.cartoonabad.data.series

import io.reactivex.Single

interface SeriesRepository {
    fun getSeries(sort: String): Single<List<Series>>

    fun getSeriesByGenre(genre_id:String):Single<List<Series>>

    fun getSeriesDetail(series_id:String):Single<Series>

}