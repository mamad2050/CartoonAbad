package ir.andromeda.cartoonabad.data.download

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "downloads")
@Parcelize
data class Downloaded(
    val duration: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val image: String,
    val name: String,
    val season_id: String,
    val path: String
) : Parcelable {
    var isDownload = false

}