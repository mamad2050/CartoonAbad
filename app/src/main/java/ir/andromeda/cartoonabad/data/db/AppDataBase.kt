package ir.andromeda.cartoonabad.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.andromeda.cartoonabad.data.downloaded.DownloadedEpisode
import ir.andromeda.cartoonabad.data.downloaded.DownloadedEpisodeLocalDataSource
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.data.episode.EpisodeLocalDataSource

@Database(entities = [Episode::class , DownloadedEpisode::class], version = 1 , exportSchema = false)

abstract class AppDataBase : RoomDatabase() {

    abstract fun episodeDao(): EpisodeLocalDataSource

    abstract fun downloadDao(): DownloadedEpisodeLocalDataSource

}