package ir.andromeda.movieado.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.movieado.common.implementSpringAnimationTrait
import ir.andromeda.movieado.data.genre.Genre
import ir.andromeda.movieado.databinding.ItemGenreBinding
import ir.andromeda.movieado.services.imageloader.ImageLoadingService

class GenreAdapter(
    private val imageLoadingService: ImageLoadingService,
    private val listener: GenreEventListener
) : RecyclerView.Adapter<GenreAdapter.Holder>() {

    var genres = ArrayList<Genre>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        holder.bindGenre(genres[position])

    override fun getItemCount(): Int = genres.size

    inner class Holder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindGenre(genre: Genre) {

            imageLoadingService.load(binding.ivGenre, genre.image)
            binding.tvGenreTitle.text = genre.title
            binding.root.implementSpringAnimationTrait()
            binding.root.setOnClickListener { }

            binding.root.setOnClickListener {
                listener.onGenreClicked(genre)
            }

        }
    }


    interface GenreEventListener {
        fun onGenreClicked(genre: Genre)
    }

}