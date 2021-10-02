package ir.andromeda.cartoonabad.feature.list

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

class EpisodeAdapter(
    private val episodes: List<Episode> = ArrayList(),
    private val imageLoadingService: ImageLoadingService,
    private val listener: EpisodeEventListener
) : RecyclerView.Adapter<EpisodeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindEpisode(episodes[position])
    }

    override fun getItemCount(): Int = episodes.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivImage = itemView.findViewById<CartoonAbadImageView>(R.id.iv_episode_image)
        private val ivFavorite = itemView.findViewById<ImageView>(R.id.iv_episode_favorite)
        private val ivDownload =
            itemView.findViewById<ImageView>(R.id.iv_episode_download)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_episode_name)
        private val tvDuration = itemView.findViewById<TextView>(R.id.tv_episode_duration)

        fun bindEpisode(episode: Episode) {
            imageLoadingService.load(ivImage as CartoonAbadImageView, episode.image)
            tvName.text = episode.name
            tvDuration.text = episode.duration

            ivDownload.setOnClickListener {

            }

            if (episode.isFavorite)
                ivFavorite.setImageResource(R.drawable.ic_baseline_star_24)
            else
                ivFavorite.setImageResource(R.drawable.ic_baseline_star_border_24)

            ivFavorite.setOnClickListener {
            listener.onFavoriteClick(episode)
                episode.isFavorite=!episode.isFavorite
                notifyItemChanged(adapterPosition)
            }

            itemView.setOnClickListener {
                listener.onEpisodeClick(episode)
            }

        }

    }
}

interface EpisodeEventListener {

    fun onEpisodeClick(episode: Episode)

    fun onFavoriteClick(episode: Episode)

}