package ir.andromeda.movieado

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.facebook.drawee.backends.pipeline.Fresco
import ir.andromeda.movieado.common.NOTIFICATION_CHANNEL_ID
import ir.andromeda.movieado.common.NOTIFICATION_CHANNEL_NAME
import ir.andromeda.movieado.data.banner.BannerRemoteDataSource
import ir.andromeda.movieado.data.banner.BannerRepository
import ir.andromeda.movieado.data.banner.BannerRepositoryImpl
import ir.andromeda.movieado.data.db.AppDataBase
import ir.andromeda.movieado.data.download.DownloadRepository
import ir.andromeda.movieado.data.download.DownloadRepositoryImpl
import ir.andromeda.movieado.data.episode.EpisodeRemoteDataSource
import ir.andromeda.movieado.data.episode.EpisodeRepository
import ir.andromeda.movieado.data.episode.EpisodeRepositoryImpl
import ir.andromeda.movieado.data.genre.GenreRemoteDataSource
import ir.andromeda.movieado.data.genre.GenreRepository
import ir.andromeda.movieado.data.genre.GenreRepositoryImpl
import ir.andromeda.movieado.data.message.MessageRemoteDataSource
import ir.andromeda.movieado.data.message.MessageRepository
import ir.andromeda.movieado.data.message.MessageRepositoryImpl
import ir.andromeda.movieado.data.movie.MovieRemoteDataSource
import ir.andromeda.movieado.data.movie.MovieRepository
import ir.andromeda.movieado.data.movie.MovieRepositoryImpl
import ir.andromeda.movieado.data.season.SeasonRemoteDataSource
import ir.andromeda.movieado.data.season.SeasonRepository
import ir.andromeda.movieado.data.season.SeasonRepositoryImpl
import ir.andromeda.movieado.data.subscription.SubscriptionRemoteDataSource
import ir.andromeda.movieado.data.subscription.SubscriptionRepository
import ir.andromeda.movieado.data.subscription.SubscriptionRepositoryImpl
import ir.andromeda.movieado.feature.contacts.ContactsViewModel
import ir.andromeda.movieado.feature.detail.DetailMovieViewModel
import ir.andromeda.movieado.feature.download.DownloadViewModel
import ir.andromeda.movieado.feature.bookmark.BookmarkViewModel
import ir.andromeda.movieado.feature.home.HomeViewModel
import ir.andromeda.movieado.feature.detail.DetailSeriesViewModel
import ir.andromeda.movieado.feature.genre.GenreViewModel
import ir.andromeda.movieado.feature.allMovie.AllMoviesViewModel
import ir.andromeda.movieado.feature.search.FilterFragmentViewModel
import ir.andromeda.movieado.feature.subscription.SubscriptionViewModel
import ir.andromeda.movieado.feature.search.SearchViewModel
import ir.andromeda.movieado.services.http.createApiServiceInstance
import ir.andromeda.movieado.services.imageloader.FrescoImageLoadingService
import ir.andromeda.movieado.services.imageloader.ImageLoadingService
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        Fresco.initialize(this)

        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(baseContext, config)

        Timber.plant(Timber.DebugTree())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            val notificationChannel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
                )

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val myModules = module {

            single { createApiServiceInstance() }

            single<ImageLoadingService> { FrescoImageLoadingService() }

            single { getSharedPreferences("sharedPref", MODE_PRIVATE) }

            single { Room.databaseBuilder(this@App, AppDataBase::class.java, "db_app").build() }

            factory<SeasonRepository> {
                SeasonRepositoryImpl(
                    SeasonRemoteDataSource(get())
                )
            }

            factory<MovieRepository> { MovieRepositoryImpl(MovieRemoteDataSource(get())) }

            factory<EpisodeRepository> {
                EpisodeRepositoryImpl(
                    get<AppDataBase>().episodeDao(),
                    EpisodeRemoteDataSource(get())
                )
            }

            factory<MessageRepository> { MessageRepositoryImpl(MessageRemoteDataSource(get())) }

            factory<DownloadRepository> { DownloadRepositoryImpl(get<AppDataBase>().downloadDao()) }

            factory<GenreRepository> { GenreRepositoryImpl(GenreRemoteDataSource(get())) }

            factory<BannerRepository> { BannerRepositoryImpl(BannerRemoteDataSource(get())) }

            factory<SubscriptionRepository> {
                SubscriptionRepositoryImpl(
                    SubscriptionRemoteDataSource(get())
                )
            }

            viewModel { HomeViewModel(get(), get(), get()) }
            viewModel { BookmarkViewModel(get()) }
            viewModel { ContactsViewModel(get()) }
            viewModel { DownloadViewModel(get()) }
            viewModel { SubscriptionViewModel(get()) }
            viewModel { (bundle: Bundle) ->
                DetailSeriesViewModel(
                    bundle,
                    get(),
                    get(),
                    get(),
                    get()
                )
            }
            viewModel { (bundle: Bundle) -> DetailMovieViewModel(bundle, get(), get()) }
            viewModel { (bundle: Bundle) -> AllMoviesViewModel(bundle, get()) }
            viewModel { SearchViewModel(get()) }
            viewModel { (bundle: Bundle) -> GenreViewModel(bundle, get()) }
            viewModel { FilterFragmentViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }

    }

}