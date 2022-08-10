package ir.andromeda.cartoonabad

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.room.Room
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.facebook.drawee.backends.pipeline.Fresco
import ir.andromeda.cartoonabad.common.NOTIFICATION_CHANNEL_ID
import ir.andromeda.cartoonabad.common.NOTIFICATION_CHANNEL_NAME
import ir.andromeda.cartoonabad.data.cartoon.CartoonRemoteDataSource
import ir.andromeda.cartoonabad.data.cartoon.CartoonRepository
import ir.andromeda.cartoonabad.data.cartoon.CartoonsRepositoryImpl
import ir.andromeda.cartoonabad.data.db.AppDataBase
import ir.andromeda.cartoonabad.data.download.DownloadedRepository
import ir.andromeda.cartoonabad.data.download.DownloadedRepositoryImpl
import ir.andromeda.cartoonabad.data.episode.EpisodeRepository
import ir.andromeda.cartoonabad.data.episode.EpisodeRepositoryImpl
import ir.andromeda.cartoonabad.data.genre.GenreRemoteDataSource
import ir.andromeda.cartoonabad.data.genre.GenreRepository
import ir.andromeda.cartoonabad.data.genre.GenreRepositoryImpl
import ir.andromeda.cartoonabad.data.message.MessageRemoteDataSource
import ir.andromeda.cartoonabad.data.message.MessageRepository
import ir.andromeda.cartoonabad.data.message.MessageRepositoryImpl
import ir.andromeda.cartoonabad.data.season.SeasonRemoteDataSource
import ir.andromeda.cartoonabad.data.season.SeasonRepository
import ir.andromeda.cartoonabad.data.season.SeasonRepositoryImpl
import ir.andromeda.cartoonabad.data.series.SeriesRemoteDataSource
import ir.andromeda.cartoonabad.data.series.SeriesRepository
import ir.andromeda.cartoonabad.data.series.SeriesRepositoryImpl
import ir.andromeda.cartoonabad.data.subscription.SubscriptionRemoteDataSource
import ir.andromeda.cartoonabad.data.subscription.SubscriptionRepository
import ir.andromeda.cartoonabad.data.subscription.SubscriptionRepositoryImpl
import ir.andromeda.cartoonabad.feature.contacts.ContactsViewModel
import ir.andromeda.cartoonabad.feature.detail.DetailCartoonViewModel
import ir.andromeda.cartoonabad.feature.dwnloaded.DownloadedViewModel
import ir.andromeda.cartoonabad.feature.favorite.FavoriteViewModel
import ir.andromeda.cartoonabad.feature.home.HomeViewModel
import ir.andromeda.cartoonabad.feature.detail.DetailSeriesViewModel
import ir.andromeda.cartoonabad.feature.genre.GenreViewModel
import ir.andromeda.cartoonabad.feature.purchase.PurchaseViewModel
import ir.andromeda.cartoonabad.services.http.createApiServiceInstance
import ir.andromeda.cartoonabad.services.imageloader.FrescoImageLoadingService
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Fresco.initialize(this)


        TapsellPlus.initialize(
            applicationContext,
            "dalbmfhclofnmdmmootbopmasiiiptkcqrtdobqcekrsinhgrjahabkccijdifgoorskol",
            object : TapsellPlusInitListener {
                override fun onInitializeSuccess(p0: AdNetworks?) {
                    Timber.i(p0?.name)
                }

                override fun onInitializeFailed(p0: AdNetworks?, p1: AdNetworkError?) {
                    Timber.e(p1?.errorMessage)
                }

            }
        )


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

            factory<SeriesRepository> { SeriesRepositoryImpl(SeriesRemoteDataSource(get())) }

            factory<CartoonRepository> { CartoonsRepositoryImpl(CartoonRemoteDataSource(get())) }

            factory<EpisodeRepository> { EpisodeRepositoryImpl(get<AppDataBase>().episodeDao()) }

            factory<MessageRepository> { MessageRepositoryImpl(MessageRemoteDataSource(get())) }

            factory<DownloadedRepository> { DownloadedRepositoryImpl(get<AppDataBase>().downloadDao()) }

            factory<GenreRepository> { GenreRepositoryImpl(GenreRemoteDataSource(get())) }

            factory<SubscriptionRepository> {
                SubscriptionRepositoryImpl(
                    SubscriptionRemoteDataSource(get())
                )
            }

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


            viewModel { HomeViewModel(get(), get(), get()) }
            viewModel { FavoriteViewModel(get()) }
            viewModel { GenreViewModel(get()) }
            viewModel { ContactsViewModel(get()) }
            viewModel { DownloadedViewModel(get()) }
            viewModel { PurchaseViewModel(get()) }
            viewModel { (bundle: Bundle) ->
                DetailSeriesViewModel(
                    bundle,
                    get(),
                    get(),
                    get(),
                    get()
                )
            }
            viewModel { (bundle: Bundle) -> DetailCartoonViewModel(bundle, get(), get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }

    }

}