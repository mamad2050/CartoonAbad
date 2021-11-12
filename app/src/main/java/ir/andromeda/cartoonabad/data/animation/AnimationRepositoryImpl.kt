package ir.andromeda.cartoonabad.data.animation

import io.reactivex.Single
import ir.andromeda.cartoonabad.data.AppData

class AnimationRepositoryImpl(
    private val remoteDataSource: AnimationDataSource
) : AnimationRepository {

    override fun getAnimations(): Single<List<Animation>> = remoteDataSource.getAnimations()

    override fun getVersion(): Single<AppData> = remoteDataSource.getVersion()

}