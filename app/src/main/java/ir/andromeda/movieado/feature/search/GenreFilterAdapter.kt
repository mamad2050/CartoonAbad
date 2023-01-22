package ir.andromeda.movieado.feature.search

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.movieado.common.implementSpringAnimationTrait
import ir.andromeda.movieado.data.genre.Genre
import ir.andromeda.movieado.databinding.ItemGenreFilterBinding
import ir.andromeda.movieado.services.imageloader.ImageLoadingService

class GenreFilterAdapter(
    private val genresList: ArrayList<Genre>,
    private val imageLoadingService: ImageLoadingService,
    private val listener: OnGenreEventListener
) : RecyclerView.Adapter<GenreFilterAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemGenreFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bindGenre(genresList[position])

    override fun getItemCount(): Int = genresList.size

    inner class Holder(private val binding: ItemGenreFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bindGenre(genre: Genre) {

            imageLoadingService.load(binding.ivGenre, genre.image)
            binding.tvGenreTitle.text = genre.title
            binding.root.implementSpringAnimationTrait()
            binding.root.setOnClickListener { }

            binding.root.setOnClickListener {
                listener.onGenreClick(genre)
            }

            binding.root.setOnClickListener {
//                binding.ivGenre.foreground = it.
            }

        }
    }


    interface OnGenreEventListener {
        fun onGenreClick(genre: Genre)
    }

}