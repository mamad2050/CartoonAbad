package ir.andromeda.cartoonabad.feature.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.data.season.Season
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.view.CartoonAbadImageView

class SeasonAdapter(
    private val seasons: List<Season> = ArrayList(),
    private val imageLoadingService: ImageLoadingService,
    private val context: Context,
    private val episodeListener: EpisodeEventListener,

    ) : RecyclerView.Adapter<SeasonAdapter.MyViewHolder>() {

    private lateinit var episodeAdapter: EpisodeAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_season, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindSeason(seasons[position])
    }

    override fun getItemCount(): Int = seasons.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvName = itemView.findViewById<TextView>(R.id.tv_season_name)
        private val rvEpisodes = itemView.findViewById<RecyclerView>(R.id.rv_season_episodes)
        private val tvEpisodeSize =
            itemView.findViewById<TextView>(R.id.tv_season_episodeSize)
        private val ivArrow = itemView.findViewById<ImageView>(R.id.iv_season_arrow)

        fun bindSeason(season: Season) {
            tvName.text = season.name
            tvEpisodeSize.text = season.episodeList.size.toString()

            rvEpisodes.visibility = if (season.visibility) View.VISIBLE else View.GONE
            ivArrow.setImageResource(
                if (season.visibility) R.drawable.ic_baseline_arrow_drop_up_24
                else R.drawable.ic_baseline_arrow_drop_down_24
            )

            itemView.setOnClickListener {
                season.visibility = !season.visibility
                notifyItemChanged(absoluteAdapterPosition)
            }

            rvEpisodes.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            episodeAdapter =
                EpisodeAdapter(season.episodeList, imageLoadingService, episodeListener)
            rvEpisodes.adapter = episodeAdapter

        }

    }

    fun updateEpisode(episode: Episode) {
        episodeAdapter.updateEpisode(episode)
    }

}