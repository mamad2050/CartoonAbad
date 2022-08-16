package ir.andromeda.cartoonabad.feature.search

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.common.implementSpringAnimationTrait
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries
import ir.andromeda.cartoonabad.databinding.ItemCartoonSeriesBinding
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService

class SearchAdapter(
    var list: ArrayList<CombinedCartoonSeries>,
    private val imageLoadingService: ImageLoadingService,
) : RecyclerView.Adapter<SearchAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemCartoonSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val displayMetrics = DisplayMetrics()
        (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceWidth = displayMetrics.widthPixels / 3 - 12
        val deviceHeight = displayMetrics.heightPixels / 4
        binding.parentItem.layoutParams.width = deviceWidth
        binding.parentItem.layoutParams.height = deviceHeight

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size

    inner class Holder(val binding: ItemCartoonSeriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CombinedCartoonSeries) {

            imageLoadingService.load(binding.ivImage, item.image)
            binding.tvName.text = item.name
            binding.tvRate.text = item.rate

            binding.ivImage.setOnClickListener {

            }

            binding.root.implementSpringAnimationTrait()
        }
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }
}