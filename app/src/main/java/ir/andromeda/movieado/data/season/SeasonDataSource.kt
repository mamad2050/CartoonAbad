package ir.andromeda.movieado.data.season

import io.reactivex.Single

interface SeasonDataSource {
    fun getSeasons(series_id: String): Single<List<Season>>
}