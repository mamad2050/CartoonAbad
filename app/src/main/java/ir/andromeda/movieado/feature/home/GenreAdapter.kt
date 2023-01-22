package ir.andromeda.movieado.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.movieado.common.implementSpringAnimationTrait
import ir.andromeda.movieado.data.genre.Genre
import ir.andromeda.movieado.databinding.ItemGenreBinding
import ir.andromeda.movieado.services.imageloader.ImageLoadingService

class GenreAdapter(
    private val genresList: ArrayList<Genre>,
    private val imageLoadingService: ImageLoadingService,
    private val listener: OnGenreEventListener
) : RecyclerView.Adapter<GenreAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemGenreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
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
            binding.root.setOnClickListener { }

            binding.root.setOnClickListener {
                listener.onGenreClick(genre)
            }

        }
    }


    interface OnGenreEventListener {
        fun onGenreClick(genre: Genre)
    }

}