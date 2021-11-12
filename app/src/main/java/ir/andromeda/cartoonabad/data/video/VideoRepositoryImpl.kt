package ir.andromeda.cartoonabad.data.video

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.AppData

class VideoRepositoryImpl(
    private val remoteDataSource: VideoDataSource
) : VideoRepository {

    override fun getAnimations(): Single<List<Video>> = remoteDataSource.getAnimations()

    override fun getVersion(): Single<AppData> = remoteDataSource.getVersion()

}