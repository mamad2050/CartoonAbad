package ir.andromeda.cartoonabad.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.databinding.ItemAnimationBinding

class SeriesAdapter(
    private val seriesList:ArrayList<Series> ,
    val imageLoadingService: ImageLoadingService,
//    val animationEventListener: OnItemEventListener<Animation>?
) : RecyclerView.Adapter<SeriesAdapter.Holder>() {
//    private val displayMetrics = DisplayMetrics()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

//        (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val binding = ItemAnimationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bindAnimation(seriesList[position])


    override fun getItemCount(): Int = seriesList.size

    inner class Holder(val binding: ItemAnimationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindAnimation(series: Series) {

            imageLoadingService.load(binding.ivAnimation,series.name)
            binding.tvAnimationName.text = series.name
//            if (series.no_episodes.toInt() == 0) {
//                ivAnimation.alpha = 0.4f
//                tvComingSoon.visibility = View.VISIBLE
//            }

//            val deviceWidth =
//                displayMetrics.widthPixels / 3 - convertDpToPixel(8.toFloat(), ivAnimation.context)
//            val deviceHeight = deviceWidth * 1.5
//
//            ivAnimation.layoutParams.width = deviceWidth.toInt()
//            ivAnimation.layoutParams.height = deviceHeight.toInt()

//            itemView.implementSpringAnimationTrait()
//            itemView.setOnClickListener {
//                animationEventListener?.onCLick(series)
//            }

        }
    }

}