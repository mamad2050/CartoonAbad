package ir.andromeda.cartoonabad.data.episode

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "episodes")
@Parcelize
data class Episode(
    @PrimaryKey
    @SerializedName("_id") val id: String,
    @SerializedName("image") val image: String,
    @SerializedName("name") val name: String,
    @SerializedName("season_id") val seasonId: String,
    @SerializedName("url") val url: String,
    @SerializedName("duration") val duration: String,
) : Parcelable {
    var isBookmarked = false
    var isDownloaded = false
}