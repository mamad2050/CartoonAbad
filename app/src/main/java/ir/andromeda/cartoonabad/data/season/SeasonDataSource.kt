package ir.andromeda.cartoonabad.data.season

import io.reactivex.Single

interface SeasonDataSource {
    fun getSeasons(animationId: Int): Single<List<Season>>
}