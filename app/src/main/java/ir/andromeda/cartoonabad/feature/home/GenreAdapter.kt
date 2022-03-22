package ir.andromeda.cartoonabad.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.common.implementSpringAnimationTrait
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.databinding.ItemGenreBinding
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService

class GenreAdapter(
    private val genresList: ArrayList<Genre>,
    private val imageLoadingService: ImageLoadingService
) : RecyclerView.Adapter<GenreAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bindGenre(genresList[position])

    override fun getItemCount(): Int = genresList.size

    inner class Holder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindGenre(genre: Genre) {

            imageLoadingService.load(binding.ivGenre, genre.image)
            binding.tvGenreTitle.text = genre.title
            binding.root.implementSpringAnimationTrait()
            binding.root.setOnClickListener {  }

        }
    }

}