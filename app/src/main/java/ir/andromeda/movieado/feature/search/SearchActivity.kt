package ir.andromeda.movieado.feature.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.GridLayoutManager
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.genre.Genre
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.databinding.ActivitySearchBinding
import ir.andromeda.movieado.feature.common.MovieAdapter
import ir.andromeda.movieado.feature.detail.DetailMovieActivity
import ir.andromeda.movieado.feature.detail.DetailSeriesActivity
import ir.andromeda.movieado.services.imageloader.ImageLoadingService
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : MovieadoActivity(),
    MovieAdapter.MovieEventListener,
    FilterBottomSheetDialog.OnFilterListener {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel()
    private val imageLoadingService: ImageLoadingService by inject()
    private val movieAdapter by lazy {
        MovieAdapter(this, imageLoadingService, ItemScale.LARGE, true)
    }
    private val filterBottomSheet = FilterBottomSheetDialog(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvResult.layoutManager = GridLayoutManager(this, 3)
        binding.rvResult.adapter = movieAdapter

        viewModel.progressBarLiveData.observe(this) {
            if (it) {
                binding.rvResult.visibility = View.GONE
                binding.spinkit.visibility = View.VISIBLE
            } else {
                binding.rvResult.visibility = View.VISIBLE
                binding.spinkit.visibility = View.GONE
            }
        }


        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(word: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (word!!.isEmpty()) {
                    movieAdapter.clear()
                    binding.layoutSearchNotFound.visibility = View.GONE
                    binding.ivClearSearch.visibility = View.GONE
                } else
                    binding.ivClearSearch.visibility = View.VISIBLE

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(v.text.toString())
            }
            false
        }

        viewModel.searchLiveData.observe(this) {
            movieAdapter.movieList = it as ArrayList<Movie>
        }

        viewModel.showNotFoundState.observe(this) {
            if (it) {
                movieAdapter.clear()
                binding.layoutSearchNotFound.visibility = View.VISIBLE
            } else {
                binding.layoutSearchNotFound.visibility = View.GONE
            }
        }

        binding.ivClearSearch.setOnClickListener {
            binding.etSearch.text.clear()
        }

        binding.fabFilter.setOnClickListener {
            showFilterBottomSheet()
        }


    }

    override fun onApplyFilter(sortBy: String?, selectedGenres: List<Genre>?) {
        TODO("Not yet implemented")
    }

    private fun showFilterBottomSheet() {
        filterBottomSheet.show(supportFragmentManager, FILTER_FRAGMENT_TAG)
    }

    override fun onMovieClicked(movie: Movie) {
        if (movie.type == TYPE_MOVIE || movie.type == TYPE_ANIMATION_MOVIE) {
            startActivity(Intent(this, DetailMovieActivity::class.java).apply {
                putExtra(EXTRA_KEY_ID, movie.id)
            })
        } else if (movie.type == TYPE_SERIES || movie.type == TYPE_ANIMATION_SERIES) {
            startActivity(Intent(this, DetailSeriesActivity::class.java).apply {
                putExtra(EXTRA_KEY_ID, movie.id)
            })
        }
    }
}