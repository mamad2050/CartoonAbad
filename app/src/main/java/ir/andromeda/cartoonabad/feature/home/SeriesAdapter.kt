package ir.andromeda.cartoonabad.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.common.implementSpringAnimationTrait
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.databinding.ItemCartoonSeriesBinding

class SeriesAdapter(
    private val seriesList:List<Series> ,
    val imageLoadingService: ImageLoadingService) : RecyclerView.Adapter<SeriesAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding = ItemCartoonSeriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bindSeries(seriesList[position])


    override fun getItemCount(): Int = seriesList.size

    inner class Holder(val binding: ItemCartoonSeriesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindSeries(series: Series) {

            imageLoadingService.load(binding.ivImage,series.name)
            binding.tvName.text = series.name

            binding.root.implementSpringAnimationTrait()

        }
    }

}