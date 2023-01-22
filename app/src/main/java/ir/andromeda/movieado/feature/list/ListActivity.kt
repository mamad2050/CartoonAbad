package ir.andromeda.movieado.feature.list

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.genre.Genre
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.databinding.ActivityListBinding
import ir.andromeda.movieado.feature.common.MovieAdapter
import ir.andromeda.movieado.feature.detail.DetailMovieActivity
import ir.andromeda.movieado.feature.detail.DetailSeriesActivity
import ir.andromeda.movieado.feature.search.FilterBottomSheetDialog
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ListActivity : MovieadoActivity(), MovieAdapter.MovieEventListener,
    FilterBottomSheetDialog.OnFilterListener {

    private lateinit var binding: ActivityListBinding
    private val viewModel: ListViewModel by viewModel { parametersOf(intent.extras) }
    private val movieAdapter = MovieAdapter(this, get(), ItemScale.LARGE)
    private val filterBottomSheet = FilterBottomSheetDialog(this)
    var currentPage = 1
    var hasNextPage = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvList.layoutManager = GridLayoutManager(this, 3)
        binding.tvTitleToolbar.text = intent.getStringExtra(EXTRA_KEY_TITLE)

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.rvList.adapter = movieAdapter

        viewModel.moviesLiveData.observe(this) {
            if (it.isEmpty()) {
                hasNextPage = false
            }

            if (currentPage == 1) {
                movieAdapter.setData(it)
            } else {
                movieAdapter.addNewData(it)
                binding.rvList.smoothScrollToPosition(movieAdapter.itemCount - 1)
            }
        }

        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (hasNextPage) {
                        when (intent.getStringExtra(EXTRA_KEY_MODE)) {

                            LIST_LATEST_MOVIES ->
                                viewModel.getMoviesList(
                                    SORT_BY_LATEST,
                                    TYPE_MOVIE,
                                    ++currentPage
                                )

                            LIST_LATEST_SERIES ->
                                viewModel.getMoviesList(
                                    SORT_BY_LATEST,
                                    TYPE_SERIES,
                                    ++currentPage
                                )

                            LIST_LATEST_ANIMATIONS ->
                                viewModel.getMoviesList(
                                    SORT_BY_LATEST,
                                    TYPE_ANIMATION,
                                    ++currentPage
                                )

                            LIST_POPULAR_MOVIES ->
                                viewModel.getMoviesList(
                                    SORT_BY_IMDB,
                                    TYPE_MOVIE,
                                    ++currentPage
                                )

                            LIST_POPULAR_SERIES ->
                                viewModel.getMoviesList(
                                    SORT_BY_IMDB,
                                    TYPE_SERIES,
                                    ++currentPage
                                )

                            LIST_POPULAR_ANIMATIONS ->
                                viewModel.getMoviesList(
                                    SORT_BY_IMDB,
                                    TYPE_ANIMATION,
                                    ++currentPage
                                )
                        }
                    }
                }
            }
        })

        binding.fabFilter.setOnClickListener {
            showFilterBottomSheet()
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

    private fun showFilterBottomSheet() {
        filterBottomSheet.show(supportFragmentManager, FILTER_FRAGMENT_TAG)
    }

    override fun onApplyFilter(sortBy: String?, selectedGenres: List<Genre>?) {

    }
}