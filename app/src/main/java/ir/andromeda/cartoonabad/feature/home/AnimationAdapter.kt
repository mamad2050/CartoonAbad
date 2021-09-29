package ir.andromeda.cartoonabad.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.implementSpringAnimationTrait
import ir.andromeda.cartoonabad.data.animation.Animation
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.view.CartoonAbadImageView

class AnimationAdapter(
    private val animations: List<Animation>,
    val imageLoadingService: ImageLoadingService,
    val animationEventListener: OnAnimationEventListener
) : RecyclerView.Adapter<AnimationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_animation, parent, false)
        )
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        myViewHolder.bindAnimation(animations[position])
    }

    override fun getItemCount(): Int = animations.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivAnimation = itemView.findViewById<CartoonAbadImageView>(R.id.ivAnimation)
        private val tvAnimation = itemView.findViewById<TextView>(R.id.tvName)
        fun bindAnimation(animation: Animation) {
            imageLoadingService.load(ivAnimation as CartoonAbadImageView, animation.image)
            tvAnimation.text = animation.name


            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                animationEventListener.onCLick(animation)
            }

        }
    }

    interface OnAnimationEventListener {
        fun onCLick(animation: Animation)
    }
}