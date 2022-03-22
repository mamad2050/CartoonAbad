package ir.andromeda.cartoonabad.feature.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.common.implementSpringAnimationTrait
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.databinding.ItemCartoonSeriesBinding
import ir.andromeda.cartoonabad.databinding.ItemGenreBinding
import ir.andromeda.cartoonabad.feature.detail.DetailCartoonActivity
import ir.andromeda.cartoonabad.feature.detail.DetailSeriesActivity
import ir.andromeda.cartoonabad.view.CartoonAbadImageView

class SeriesAdapter(
    private val context: Context,
    private val seriesList: ArrayList<Series>,
    private val imageLoadingService: ImageLoadingService
) : RecyclerView.Adapter<SeriesAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCartoonSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bindSeries(seriesList[position])

    override fun getItemCount(): Int = seriesList.size

    inner class Holder(val binding: ItemCartoonSeriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindSeries(series: Series) {

            imageLoadingService.load(binding.ivImage, series.image)
            binding.tvName.text = series.name

            binding.root.implementSpringAnimationTrait()

            binding.root.setOnClickListener {
                context.startActivity(Intent(context, DetailSeriesActivity::class.java).apply {
                    putExtra(EXTRA_KEY_DATA,series)
                })
            }

        }
    }

}