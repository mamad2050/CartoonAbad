package ir.andromeda.cartoonabad.feature.dwnloaded

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.data.download.Download
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.view.CartoonAbadImageView

class DownloadedAdapter(

    private val downloadList: MutableList<Download>,
    private val imageLoadingService: ImageLoadingService,
    private val listener: EpisodeEventListener

) : RecyclerView.Adapter<DownloadedAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindEpisode(downloadList[position])
    }

    override fun getItemCount(): Int = downloadList.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage = itemView.findViewById<CartoonAbadImageView>(R.id.iv_episode_image)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_episode_name)
        private val tvDuration = itemView.findViewById<TextView>(R.id.tv_episode_duration)
        private val ivDelete = itemView.findViewById<ImageView>(R.id.iv_episode_delete)

        fun bindEpisode(download: Download) {

            imageLoadingService.load(ivImage, download.image)
            tvName.text = download.name
            tvDuration.text = download.duration

            itemView.setOnClickListener { listener.onEpisodeClick(download) }

            ivDelete.setOnClickListener {
                listener.onRemoveClick(download)
                downloadList.remove(download)
                notifyItemRemoved(absoluteAdapterPosition)
            }

        }
    }

    interface EpisodeEventListener {

        fun onRemoveClick(download: Download)

        fun onEpisodeClick(download: Download)

    }
}