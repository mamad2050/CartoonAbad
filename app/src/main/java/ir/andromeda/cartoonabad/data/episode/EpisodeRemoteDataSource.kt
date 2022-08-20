package ir.andromeda.cartoonabad.data.episode

import io.reactivex.Completable
import io.reactivex.Single
import ir.andromeda.cartoonabad.services.http.ApiService

class EpisodeRemoteDataSource(private val apiService: ApiService) : EpisodeDataSource {

    override fun getBookmarkEpisodes(): Single<List<Episode>> {
        TODO("Not yet implemented")
    }

    override fun addToBookmark(episode: Episode): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromBookmark(episode: Episode): Completable {
        TODO("Not yet implemented")
    }

    override fun getAllEpisodes(seriesId: String): Single<List<Episode>> =
        apiService.getEpisodes(seriesId)

}