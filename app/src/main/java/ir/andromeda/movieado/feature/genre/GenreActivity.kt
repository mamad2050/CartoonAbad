package ir.andromeda.movieado.feature.genre

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.databinding.ActivityGenreBinding
import ir.andromeda.movieado.feature.common.MovieAdapter
import ir.andromeda.movieado.feature.detail.DetailMovieActivity
import ir.andromeda.movieado.feature.detail.DetailSeriesActivity
import ir.andromeda.movieado.services.imageloader.ImageLoadingService
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GenreActivity : MovieadoActivity(),
    MovieAdapter.MovieEventListener {

    private lateinit var binding: ActivityGenreBinding
    private val viewModel: GenreViewModel by viewModel { parametersOf(intent.extras) }
    private val imageLoadingService: ImageLoadingService by inject()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvData.layoutManager = GridLayoutManager(this, 3)
        movieAdapter = MovieAdapter(this, imageLoadingService, ItemScale.LARGE, true)
        binding.rvData.adapter = movieAdapter


        binding.tvTitleToolbar.text = intent.getStringExtra(EXTRA_KEY_TITLE)

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        viewModel.genresLiveData.observe(this) {
            movieAdapter.movieList = it as ArrayList<Movie>
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMovieClicked(movie: Movie) {
        if (movie.type == TYPE_MOVIE && movie.type == TYPE_ANIMATION_MOVIE) {
            startActivity(Intent(this, DetailMovieActivity::class.java).apply {
                putExtra(EXTRA_KEY_ID, movie.id)
            })
        } else if (movie.type == TYPE_SERIES && movie.type == TYPE_ANIMATION_SERIES) {
            startActivity(Intent(this, DetailSeriesActivity::class.java).apply {
                putExtra(EXTRA_KEY_ID, movie.id)
            })
        }
    }

}