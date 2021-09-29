package ir.andromeda.cartoonabad.feature.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.data.season.Season
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.view.CartoonAbadImageView

class SeasonAdapter(
    private val seasons: List<Season> = ArrayList(),
    private val imageLoadingService: ImageLoadingService,
    private val context: Context
) : RecyclerView.Adapter<SeasonAdapter.MyViewHolder>() {

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

        private val ivImage = itemView.findViewById<CartoonAbadImageView>(R.id.iv_season_image)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_season_name)
        private val rvEpisodes = itemView.findViewById<RecyclerView>(R.id.rv_season_episodes)
        private val tvEpisodeSize =
            itemView.findViewById<TextView>(R.id.tv_season_episodeSize)

        fun bindSeason(season: Season) {
            imageLoadingService.load(ivImage as CartoonAbadImageView, season.image)
            tvName.text = season.name
            tvEpisodeSize.text = season.episodeList.size.toString()

            itemView.setOnClickListener {

            }

            rvEpisodes.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            val episodeAdapter = EpisodeAdapter(season.episodeList, imageLoadingService)
            rvEpisodes.adapter = episodeAdapter

        }
    }
}