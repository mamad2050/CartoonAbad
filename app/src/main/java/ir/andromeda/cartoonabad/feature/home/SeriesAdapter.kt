package ir.andromeda.cartoonabad.feature.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.common.EXTRA_KEY_ID
import ir.andromeda.cartoonabad.common.ItemScale
import ir.andromeda.cartoonabad.common.OnItemEventListener
import ir.andromeda.cartoonabad.common.implementSpringAnimationTrait
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.databinding.ItemCartoonSeriesBinding
import ir.andromeda.cartoonabad.feature.detail.DetailSeriesActivity

class SeriesAdapter(
    private val seriesList: List<Series>,
    private val onSeriesItemEventListener: OnSeriesItemEventListener,
    private val imageLoadingService: ImageLoadingService,
    private val scale: ItemScale
) : RecyclerView.Adapter<SeriesAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemCartoonSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        if (scale == ItemScale.LARGE){
            val displayMetrics = DisplayMetrics()
            (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            val deviceWidth = displayMetrics.widthPixels / 3 - 12
            val deviceHeight = displayMetrics.heightPixels / 4
            binding.parentItem.layoutParams.width = deviceWidth
            binding.parentItem.layoutParams.height = deviceHeight
        }

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
            binding.tvRate.text = series.rate

            binding.root.implementSpringAnimationTrait()

            binding.root.setOnClickListener {
                onSeriesItemEventListener.clickOnSeries(series)
            }

        }
    }

    interface OnSeriesItemEventListener {
        fun clickOnSeries(series: Series)
    }
}