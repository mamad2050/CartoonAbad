package ir.andromeda.cartoonabad.feature.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.view.CartoonAbadImageView

class FavoriteAdapter(
    private val episodes: List<Episode> = ArrayList(),
    private val imageLoadingService: ImageLoadingService

) : RecyclerView.Adapter<FavoriteAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.Holder, position: Int) {
        holder.bindEpisode(episodes[position])
    }

    override fun getItemCount(): Int = episodes.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage = itemView.findViewById<CartoonAbadImageView>(R.id.iv_episode_image)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_episode_name)
        private val tvDuration = itemView.findViewById<TextView>(R.id.tv_episode_duration)
        private val ivPlay = itemView.findViewById<ImageView>(R.id.iv_episode_play)
        private val ivDelete= itemView.findViewById<ImageView>(R.id.iv_episode_delete)

        fun bindEpisode(episode: Episode) {

            imageLoadingService.load(ivImage,episode.image)
            tvName.text = episode.name
            tvDuration.text = episode.duration
        }
    }

}