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
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.databinding.ItemCartoonSeriesBinding
import ir.andromeda.cartoonabad.databinding.ItemGenreBinding
import ir.andromeda.cartoonabad.feature.detail.DetailCartoonActivity
import ir.andromeda.cartoonabad.view.CartoonAbadImageView

class CartoonAdapter(
    private val context:Context,
    private val cartoonsList: ArrayList<Cartoon>,
    private val imageLoadingService: ImageLoadingService
) : RecyclerView.Adapter<CartoonAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCartoonSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            binding.root.implementSpringAnimationTrait()

            binding.root.setOnClickListener {
               context.startActivity(Intent(context,DetailCartoonActivity::class.java).apply {
                   putExtra(EXTRA_KEY_DATA,cartoon)

               })
            }

        }
    }

}