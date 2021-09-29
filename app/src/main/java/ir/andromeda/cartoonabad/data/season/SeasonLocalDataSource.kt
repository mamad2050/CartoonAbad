package ir.andromeda.cartoonabad.data.season

import io.reactivex.Single

class SeasonLocalDataSource:SeasonDataSource{
    override fun getSeasons(animationId: Int): Single<List<Season>> {
        TODO("Not yet implemented")
    }
}