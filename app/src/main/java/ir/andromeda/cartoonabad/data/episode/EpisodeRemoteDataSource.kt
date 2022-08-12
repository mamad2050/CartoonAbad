package ir.andromeda.cartoonabad.data.episode

import io.reactivex.Completable
import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class EpisodeRemoteDataSource(private val apiService: ApiService) : EpisodeDataSource {

    override fun getFavoriteEpisodes(): Single<List<Episode>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(episode: Episode): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(episode: Episode): Completable {
        TODO("Not yet implemented")
    }

    override fun getAllEpisodes(seriesId: String): Single<List<Episode>> =
        apiService.getEpisodes(seriesId)

}