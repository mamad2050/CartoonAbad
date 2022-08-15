package ir.andromeda.cartoonabad.feature.home

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.common.ItemScale
import ir.andromeda.cartoonabad.common.OnItemEventListener
import ir.andromeda.cartoonabad.common.implementSpringAnimationTrait
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.databinding.ItemCartoonSeriesBinding

class CartoonAdapter(
    private val cartoonsList: List<Cartoon>,
    private val onCartoonItemEventListener: OnCartoonItemEventListener,
    private val imageLoadingService: ImageLoadingService,
    private val scale: ItemScale
) : RecyclerView.Adapter<CartoonAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemCartoonSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        if (scale == ItemScale.LARGE) {
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
        holder.bindCartoon(cartoonsList[position])

    override fun getItemCount(): Int = cartoonsList.size

    inner class Holder(val binding: ItemCartoonSeriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCartoon(cartoon: Cartoon) {

            imageLoadingService.load(binding.ivImage, cartoon.image)
            binding.tvName.text = cartoon.name
            binding.tvRate.text = cartoon.rate.toString()

            binding.root.implementSpringAnimationTrait()

            binding.root.setOnClickListener {
                onCartoonItemEventListener.clickOnCartoon(cartoon)
            }

        }
    }

    interface OnCartoonItemEventListener {
        fun clickOnCartoon(cartoon: Cartoon)
    }
}

