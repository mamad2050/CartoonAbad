package ir.andromeda.movieado.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.andromeda.movieado.data.download.Download
import ir.andromeda.movieado.data.download.DownloadLocalDataSource
import ir.andromeda.movieado.data.episode.Episode
import ir.andromeda.movieado.data.episode.EpisodeLocalDataSource

@Database(entities = [Episode::class , Download::class], version = 1 , exportSchema = false)

abstract class AppDataBase : RoomDatabase() {

    abstract fun episodeDao(): EpisodeLocalDataSource

    abstract fun downloadDao(): DownloadLocalDataSource

}