package ir.andromeda.movieado.feature.common

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.movieado.R
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.databinding.ItemMovieBinding
import ir.andromeda.movieado.services.imageloader.ImageLoadingService

class MovieAdapter(
    private val movieEventListener: MovieEventListener,
    private val imageLoadingService: ImageLoadingService,
    private val scale: ItemScale,
    private val showBadge: Boolean = false
) : RecyclerView.Adapter<MovieAdapter.Holder>() {

    private var movieList = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
        holder.bindMovie(movieList[position])

    override fun getItemCount(): Int = movieList.size

    inner class Holder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindMovie(movie: Movie) {
            imageLoadingService.load(binding.ivImage, movie.image)
            binding.tvName.text = movie.name
            binding.tvRate.text = movie.imdb.toString()

            binding.root.implementSpringAnimationTrait()

            binding.root.setOnClickListener {
                movieEventListener.onMovieClicked(movie)
            }

            if (showBadge) {
                if (movie.type == TYPE_SERIES && movie.type == TYPE_ANIMATION_SERIES) {
                    binding.ivType.setImageResource(R.drawable.series)
                } else if (movie.type == TYPE_MOVIE && movie.type == TYPE_ANIMATION_MOVIE) {
                    binding.ivType.setImageResource(R.drawable.cartoon)
                }
            }
        }
    }

    fun setData(data: List<Movie>) {
        movieList.addAll(data)
        notifyDataSetChanged()
    }

    fun addNewData(data: List<Movie>) {
        val prevItemsCount = itemCount
        movieList.addAll(data)
        notifyItemRangeInserted(prevItemsCount, itemCount)
    }

    fun clear() {
        movieList.clear()
        notifyDataSetChanged()
    }

    interface MovieEventListener {
        fun onMovieClicked(movie: Movie)
    }
}

