package ir.andromeda.movieado.feature.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.movieado.R
import ir.andromeda.movieado.data.episode.Episode
import ir.andromeda.movieado.databinding.ItemEpisodeBinding
import ir.andromeda.movieado.services.imageloader.ImageLoadingService

class EpisodeAdapter(
    private val imageLoadingService: ImageLoadingService,
    private val listener: EpisodeEventListener,
) : RecyclerView.Adapter<EpisodeAdapter.Holder>() {

    var episodes = ArrayList<Episode>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindEpisode(episodes[position])
    }

    override fun getItemCount(): Int = episodes.size

    inner class Holder(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindEpisode(episode: Episode) {

            imageLoadingService.load(binding.ivImage, episode.imageUrl)
            binding.tvName.text = episode.name
            binding.tvDuration.text = episode.duration

//            if (episode.isFavorite)
//                binding.ivFavorite.setImageResource(R.drawable.ic_baseline_star_24)
//            else
//                binding.ivFavorite.setImageResource(R.drawable.ic_baseline_star_border_24)
//
//
//            if (episode.isDownloaded)
//                binding.ivDownload.setImageResource(R.drawable.ic_check)
//            else
//                binding.ivDownload.setImageResource(R.drawable.ic_file_download_white_24dp)

//            binding.ivFavorite.setOnClickListener {
//                listener.onFavoriteClick(episode)
//                episode.isFavorite = !episode.isFavorite
//                notifyItemChanged(absoluteAdapterPosition)
//            }

            binding.ivBookmark.setOnClickListener {
                binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_fill_white)
            }

            itemView.setOnClickListener {
                listener.onEpisodeClicked(episode)
            }

//            binding.ivDownload.setOnClickListener {
//                if (PurchaseContainer.purchaseInfo == null) {
//                    EventBus.getDefault().post(CartoonAbadExceptionMapper.map(PurchaseException()))
//                } else {
//                    listener.onDownloadClick(episode)
//                }
//            }
        }
    }

}


interface EpisodeEventListener {

    fun onEpisodeClicked(episode: Episode)

    fun onFavoriteClicked(episode: Episode)

    fun onDownloadClicked(episode: Episode)

}