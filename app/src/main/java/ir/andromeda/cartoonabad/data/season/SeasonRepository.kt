package ir.andromeda.cartoonabad.data.season

import io.reactivex.Single

interface SeasonRepository {
    fun getSeasons(animationId: Int): Single<List<Season>>
}