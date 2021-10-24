package ir.andromeda.cartoonabad.feature.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadExceptionMapper
import ir.andromeda.cartoonabad.data.PurchaseContainer
import ir.andromeda.cartoonabad.data.PurchaseException
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.view.CartoonAbadImageView
import org.greenrobot.eventbus.EventBus

class EpisodeAdapter(
    private val episodes: List<Episode> = ArrayList(),
    private val imageLoadingService: ImageLoadingService,
    private val listener: EpisodeEventListener,

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
        private val ivDownload = itemView.findViewById<ImageView>(R.id.iv_episode_download)
        private val ivLock = itemView.findViewById<ImageView>(R.id.iv_episode_lock)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_episode_name)
        private val tvDuration = itemView.findViewById<TextView>(R.id.tv_episode_duration)

        fun bindEpisode(episode: Episode) {

            imageLoadingService.load(ivImage as CartoonAbadImageView, episode.image)
            tvName.text = episode.name
            tvDuration.text = episode.duration

            if (episode.isFavorite)
                ivFavorite.setImageResource(R.drawable.ic_baseline_star_24)
            else
                ivFavorite.setImageResource(R.drawable.ic_baseline_star_border_24)

            if (PurchaseContainer.purchaseInfo == null && absoluteAdapterPosition > 9) {
                ivFavorite.visibility = View.INVISIBLE
                ivDownload.visibility = View.INVISIBLE
                ivLock.visibility = View.VISIBLE
            } else {
                ivFavorite.visibility = View.VISIBLE
                ivDownload.visibility = View.VISIBLE
                ivLock.visibility = View.INVISIBLE
            }

            ivFavorite.setOnClickListener {
                listener.onFavoriteClick(episode)
                episode.isFavorite = !episode.isFavorite
                notifyItemChanged(absoluteAdapterPosition)
            }

            itemView.setOnClickListener {
                if (PurchaseContainer.purchaseInfo == null && absoluteAdapterPosition > 9) {
                    EventBus.getDefault().post(CartoonAbadExceptionMapper.map(PurchaseException()))
                } else {
                    listener.onEpisodeClick(episode)
                }
            }

            ivDownload.setOnClickListener {
                listener.onDownloadClick(episode)
            }

        }

    }
}


interface EpisodeEventListener {

    fun onEpisodeClick(episode: Episode)

    fun onFavoriteClick(episode: Episode)

    fun onDownloadClick(episode: Episode)

}