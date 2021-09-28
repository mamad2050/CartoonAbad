package ir.andromeda.cartoonabad.data.animation

import io.reactivex.Single

class AnimationRepositoryImpl(
    private val remoteDataSource: AnimationDataSource
) : AnimationRepository{

    override fun getProducts(): Single<List<Animation>> = remoteDataSource.getAnimations()
}