package ir.andromeda.cartoonabad

import android.app.Application
import androidx.room.Room
import com.facebook.drawee.backends.pipeline.Fresco
import ir.andromeda.cartoonabad.data.animation.AnimationRemoteDataSource
import ir.andromeda.cartoonabad.data.animation.AnimationRepository
import ir.andromeda.cartoonabad.data.animation.AnimationRepositoryImpl
import ir.andromeda.cartoonabad.data.db.AppDataBase
import ir.andromeda.cartoonabad.data.episode.EpisodeRepository
import ir.andromeda.cartoonabad.data.episode.EpisodeRepositoryImpl
import ir.andromeda.cartoonabad.data.season.SeasonRemoteDataSource
import ir.andromeda.cartoonabad.data.season.SeasonRepository
import ir.andromeda.cartoonabad.data.season.SeasonRepositoryImpl
import ir.andromeda.cartoonabad.feature.favorite.FavoriteViewModel
import ir.andromeda.cartoonabad.feature.home.HomeViewModel
import ir.andromeda.cartoonabad.feature.list.ListViewModel
import ir.andromeda.cartoonabad.services.http.createApiServiceInstance
import ir.andromeda.cartoonabad.services.imageloader.FrescoImageLoadingService
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Fresco.initialize(this)

        Timber.plant(Timber.DebugTree())

        val myModules = module {

            single { createApiServiceInstance() }
            single<ImageLoadingService> { FrescoImageLoadingService() }

            single { Room.databaseBuilder(this@App,AppDataBase::class.java,"db_app").build() }

            factory<AnimationRepository> { AnimationRepositoryImpl(AnimationRemoteDataSource(get())) }
            factory<SeasonRepository> { SeasonRepositoryImpl(SeasonRemoteDataSource(get())) }
            factory<EpisodeRepository> { EpisodeRepositoryImpl(get<AppDataBase>().episodeDao()) }

            viewModel { HomeViewModel(get()) }
            viewModel { (animationId: String) -> ListViewModel(animationId, get(),get()) }
            viewModel { FavoriteViewModel(get())}
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }
    }
}