package ir.andromeda.cartoonabad.data.download

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "downloads")
@Parcelize
data class Download(
    val duration: String,
    @PrimaryKey
    val id: String,
    val imageUrl: String,
    val name: String,
    val path: String,
) : Parcelable