package ir.andromeda.cartoonabad

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.room.Room
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.facebook.drawee.backends.pipeline.Fresco
import ir.andromeda.cartoonabad.common.NOTIFICATION_CHANNEL_ID
import ir.andromeda.cartoonabad.common.NOTIFICATION_CHANNEL_NAME
import ir.andromeda.cartoonabad.data.animation.AnimationRemoteDataSource
import ir.andromeda.cartoonabad.data.animation.AnimationRepository
import ir.andromeda.cartoonabad.data.animation.AnimationRepositoryImpl
import ir.andromeda.cartoonabad.data.db.AppDataBase
import ir.andromeda.cartoonabad.data.downloaded.DownloadedEpisodeRepository
import ir.andromeda.cartoonabad.data.downloaded.DownloadedEpisodeRepositoryImpl
import ir.andromeda.cartoonabad.data.episode.EpisodeRepository
import ir.andromeda.cartoonabad.data.episode.EpisodeRepositoryImpl
import ir.andromeda.cartoonabad.data.message.MessageRemoteDataSource
import ir.andromeda.cartoonabad.data.message.MessageRepository
import ir.andromeda.cartoonabad.data.message.MessageRepositoryImpl
import ir.andromeda.cartoonabad.data.season.SeasonRemoteDataSource
import ir.andromeda.cartoonabad.data.season.SeasonRepository
import ir.andromeda.cartoonabad.data.season.SeasonRepositoryImpl
import ir.andromeda.cartoonabad.feature.contacts.ContactsViewModel
import ir.andromeda.cartoonabad.feature.dwnloaded.DownloadedViewModel
import ir.andromeda.cartoonabad.feature.favorite.FavoriteViewModel
import ir.andromeda.cartoonabad.feature.home.HomeViewModel
import ir.andromeda.cartoonabad.feature.list.ListViewModel
import ir.andromeda.cartoonabad.services.http.createApiServiceInstance
import ir.andromeda.cartoonabad.services.imageloader.FrescoImageLoadingService
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

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

            single { Room.databaseBuilder(this@App, AppDataBase::class.java, "db_app").build() }

            factory<AnimationRepository> { AnimationRepositoryImpl(AnimationRemoteDataSource(get())) }
            factory<SeasonRepository> {
                SeasonRepositoryImpl(
                    SeasonRemoteDataSource(get()),
                    get<AppDataBase>().episodeDao()
                )
            }
            factory<EpisodeRepository> { EpisodeRepositoryImpl(get<AppDataBase>().episodeDao()) }
            factory<MessageRepository> { MessageRepositoryImpl(MessageRemoteDataSource(get())) }
            factory<DownloadedEpisodeRepository> { DownloadedEpisodeRepositoryImpl(get<AppDataBase>().downloadDao()) }

            single {
                Payment(
                    this@App, PaymentConfiguration(
                        SecurityCheck.Enable(
                            rsaPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDDXVO1lzHbUz5z1ntxJoqcEinB" +
                                    "Ls1Gts0JQKdda5OdLqkBXgKDKFNsP0Pt1JW0dTNKWmj4qe0V/PxBRybjg57+a7xXdBdOCx" +
                                    "L8G9xvaF2LpgYOs0XjKM5JzIbowJ/XZ5jDLdD5IGLbtyYc4vDoo1eTXunhFIhyu76WCUZR" +
                                    "/Zug0KJ9oRWYVrgdjWY3Ylx2pUlwrNMs1PXUbcfDKUQoXyaG1P/RYLh8rR2/fvZA99sCAwEAAQ=="
                        )
                    )
                )
            }


            viewModel { HomeViewModel(get()) }
            viewModel { (animationId: String) -> ListViewModel(animationId, get(), get()) }
            viewModel { FavoriteViewModel(get()) }
            viewModel { ContactsViewModel(get()) }
            viewModel { DownloadedViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }

    }

}