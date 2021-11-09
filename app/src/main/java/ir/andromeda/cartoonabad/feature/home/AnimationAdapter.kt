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
import ir.andromeda.cartoonabad.data.animation.Animation
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.view.CartoonAbadImageView
import android.app.Activity
import ir.andromeda.cartoonabad.common.convertDpToPixel


class AnimationAdapter(
    private val animations: List<Animation> = ArrayList(),
    val imageLoadingService: ImageLoadingService,
    val animationEventListener: OnItemEventListener<Animation>?
) : RecyclerView.Adapter<AnimationAdapter.MyViewHolder>() {
    private val displayMetrics = DisplayMetrics()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {



        (parent.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_animation, parent, false)
        )
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        myViewHolder.bindAnimation(animations[position])


    }

    override fun getItemCount(): Int = animations.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivAnimation = itemView.findViewById<CartoonAbadImageView>(R.id.iv_animation)
        private val tvAnimation = itemView.findViewById<TextView>(R.id.tv_animation_name)


        fun bindAnimation(animation: Animation) {
            imageLoadingService.load(ivAnimation as CartoonAbadImageView, animation.image)
            tvAnimation.text = animation.name




            val deviceWidth =
                displayMetrics.widthPixels / 3 - convertDpToPixel(6.toFloat(), ivAnimation.context)
            val deviceHeight = deviceWidth * 1.5

            ivAnimation.layoutParams.width = deviceWidth.toInt()
            ivAnimation.layoutParams.height = deviceHeight.toInt()

            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                animationEventListener?.onCLick(animation)
            }

        }
    }

}