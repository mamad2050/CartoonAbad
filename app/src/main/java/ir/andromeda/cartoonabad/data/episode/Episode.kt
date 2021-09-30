package ir.andromeda.cartoonabad.data.episode

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "episodes")
@Parcelize
data class Episode(
    val duration: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val image: String,
    val name: String,
    val season_id: String,
    val url: String
) : Parcelable {
    var isFavorite = false
}