package ir.andromeda.cartoonabad.data.season

import io.reactivex.Single

interface SeasonRepository {
    fun getSeasons(series_id: Int): Single<List<Season>>
}