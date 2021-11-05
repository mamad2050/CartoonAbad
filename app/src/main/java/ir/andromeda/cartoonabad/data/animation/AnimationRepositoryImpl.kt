package ir.andromeda.cartoonabad.data.animation

import io.reactivex.Single

class AnimationRepositoryImpl(
    private val remoteDataSource: AnimationDataSource
) : AnimationRepository {

    override fun getAnimations(): Single<List<Animation>> = remoteDataSource.getAnimations()

    override fun getVersion(): Single<Int> = remoteDataSource.getVersion()

}