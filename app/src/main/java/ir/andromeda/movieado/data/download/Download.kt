package ir.andromeda.movieado.data.download

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "downloads")
@Parcelize
data class Download(
    @PrimaryKey
    val id: String,
    val imageUrl: String,
    val duration: String,
    val name: String,
    val path: String,
) : Parcelable