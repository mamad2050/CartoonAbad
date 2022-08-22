package ir.andromeda.cartoonabad.feature

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CARTOON
import ir.andromeda.cartoonabad.common.OnItemEventListener
import ir.andromeda.cartoonabad.common.SERIES
import ir.andromeda.cartoonabad.common.implementSpringAnimationTrait
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries
import ir.andromeda.cartoonabad.databinding.ItemCartoonSeriesBinding
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService

class CombinedCartoonSeriesAdapter(
    private val imageLoadingService: ImageLoadingService,
    private val listener: OnItemEventListener<CombinedCartoonSeries>
) : RecyclerView.Adapter<CombinedCartoonSeriesAdapter.Holder>() {

    var result = ArrayList<CombinedCartoonSeries>()

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

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(result[position])

    override fun getItemCount(): Int = result.size

    inner class Holder(val binding: ItemCartoonSeriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CombinedCartoonSeries) {

            imageLoadingService.load(binding.ivImage, item.image)
            binding.tvName.text = item.name
            binding.tvRate.text = item.rate

            if (item.type == SERIES)
                binding.ivType.setImageResource(R.drawable.series)

            if (item.type == CARTOON)
                binding.ivType.setImageResource(R.drawable.cartoon)

            binding.ivImage.setOnClickListener {
                listener.clickOnItem(item)
            }
            binding.root.implementSpringAnimationTrait()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        result.clear()
        notifyDataSetChanged()
    }

    fun setData(data: ArrayList<CombinedCartoonSeries>) {
        result = data
        notifyDataSetChanged()
    }
}