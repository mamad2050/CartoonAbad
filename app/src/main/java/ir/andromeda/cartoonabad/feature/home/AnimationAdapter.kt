package ir.andromeda.cartoonabad.feature.home

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.OnItemEventListener
import ir.andromeda.cartoonabad.common.implementSpringAnimationTrait
import ir.andromeda.cartoonabad.data.video.Video
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.view.CartoonAbadImageView
import android.app.Activity
import ir.andromeda.cartoonabad.common.convertDpToPixel

class AnimationAdapter(
    private val videos: List<Video> = ArrayList(),
    val imageLoadingService: ImageLoadingService,
    val videoEventListener: OnItemEventListener<Video>?
) : RecyclerView.Adapter<AnimationAdapter.MyViewHolder>() {
    private val displayMetrics = DisplayMetrics()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_animation, parent, false)
        )
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        myViewHolder.bindAnimation(videos[position])

    }

    override fun getItemCount(): Int = videos.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivAnimation = itemView.findViewById<CartoonAbadImageView>(R.id.iv_animation)
        private val tvAnimationName = itemView.findViewById<TextView>(R.id.tv_animation_name)
        private val tvAnimationCounts = itemView.findViewById<TextView>(R.id.tv_animation_counts)
        private val tvAnimationRate = itemView.findViewById<TextView>(R.id.tv_animation_rate)
        private val tvComingSoon = itemView.findViewById<TextView>(R.id.tv_coming_soon)

        fun bindAnimation(video: Video) {

            imageLoadingService.load(ivAnimation as CartoonAbadImageView, video.image)
            tvAnimationName.text = video.name

            tvAnimationCounts.text =
                video.no_seasons + " فصل" + " - " + video.no_episodes + " قسمت"

            tvAnimationRate.text = "امتیاز : " + video.rate + " از " + "10"

            if (video.no_episodes.toInt() == 0) {
                ivAnimation.alpha = 0.4f
                tvComingSoon.visibility = View.VISIBLE
            }

            val deviceWidth =
                displayMetrics.widthPixels / 3 - convertDpToPixel(8.toFloat(), ivAnimation.context)
            val deviceHeight = deviceWidth * 1.5

            ivAnimation.layoutParams.width = deviceWidth.toInt()
            ivAnimation.layoutParams.height = deviceHeight.toInt()

            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                videoEventListener?.onCLick(video)
            }

        }
    }

}