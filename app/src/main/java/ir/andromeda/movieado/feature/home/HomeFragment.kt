package ir.andromeda.movieado.feature.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.CompositeDisposable
import ir.andromeda.movieado.R
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.MovieadoEvent
import ir.andromeda.movieado.data.genre.Genre
import ir.andromeda.movieado.data.movie.Movie
import ir.andromeda.movieado.databinding.FragmentHomeBinding
import ir.andromeda.movieado.feature.common.MovieAdapter
import ir.andromeda.movieado.feature.detail.DetailMovieActivity
import ir.andromeda.movieado.feature.detail.DetailSeriesActivity
import ir.andromeda.movieado.feature.genre.GenreActivity
import ir.andromeda.movieado.feature.list.ListActivity
import ir.andromeda.movieado.feature.search.SearchActivity
import ir.andromeda.movieado.services.imageloader.ImageLoadingService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : MovieadoFragment(),
    MovieAdapter.MovieEventListener,
    GenreAdapter.OnGenreEventListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var genresAdapter: GenreAdapter
    private lateinit var latestMovieAdapter: MovieAdapter
    private lateinit var latestSeriesAdapter: MovieAdapter
    private lateinit var latestAnimationsAdapter: MovieAdapter
    private lateinit var popularMovieAdapter: MovieAdapter
    private lateinit var popularSeriesAdapter: MovieAdapter
    private lateinit var popularAnimationsAdapter: MovieAdapter

    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: HomeViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var handler: Handler
    private var bannersCount: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper()!!)

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        viewModel.bannersLiveData.observe(viewLifecycleOwner) {

            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            binding.bannerSliderViewPager.adapter = bannerSliderAdapter
            binding.sliderIndicator.setViewPager2(binding.bannerSliderViewPager)
            bannersCount = it.size

            setUpTransformer()

            binding.bannerSliderViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, 5000)
                }
            })
        }

        viewModel.genresLiveData.observe(viewLifecycleOwner) {
            binding.rvGenres.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            genresAdapter = GenreAdapter(it as ArrayList<Genre>, imageLoadingService, this)
            binding.rvGenres.adapter = genresAdapter
        }

        viewModel.latestMoviesLiveData.observe(viewLifecycleOwner) {
            binding.rvLatestMovies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            latestMovieAdapter =
                MovieAdapter(this, imageLoadingService, ItemScale.SMALL)
            binding.rvLatestMovies.adapter = latestMovieAdapter
            latestMovieAdapter.setData(it)
        }

        viewModel.latestSeriesLiveData.observe(viewLifecycleOwner) {
            binding.rvLatestSeries.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            latestSeriesAdapter = MovieAdapter(this, imageLoadingService, ItemScale.SMALL)
            binding.rvLatestSeries.adapter = latestSeriesAdapter
            latestSeriesAdapter.setData(it)
        }

        viewModel.latestAnimationsLiveData.observe(viewLifecycleOwner) {
            binding.rvLatestAnimations.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            latestAnimationsAdapter = MovieAdapter(this, imageLoadingService, ItemScale.SMALL)
            binding.rvLatestAnimations.adapter = latestAnimationsAdapter
            latestAnimationsAdapter.setData(it)
        }

        viewModel.popularMoviesLiveData.observe(viewLifecycleOwner) {
            binding.rvPopularMovies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularMovieAdapter = MovieAdapter(this, imageLoadingService, ItemScale.SMALL)
            binding.rvPopularMovies.adapter = popularMovieAdapter
            popularMovieAdapter.setData(it)
        }

        viewModel.popularSeriesLiveData.observe(viewLifecycleOwner) {
            binding.rvPopularSeries.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularSeriesAdapter = MovieAdapter(this, imageLoadingService, ItemScale.SMALL)
            binding.rvPopularSeries.adapter = popularSeriesAdapter
            popularSeriesAdapter.setData(it)
        }

        viewModel.popularAnimationsLiveData.observe(viewLifecycleOwner) {
            binding.rvPopularAnimations.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularAnimationsAdapter = MovieAdapter(this, imageLoadingService, ItemScale.SMALL)
            binding.rvPopularAnimations.adapter = popularAnimationsAdapter
            popularAnimationsAdapter.setData(it)
        }

        binding.tvMoreLatestMovies.setOnClickListener {
            startActivity(Intent(requireActivity(), ListActivity::class.java).apply {
                putExtra(EXTRA_KEY_MODE, LIST_LATEST_MOVIES)
                putExtra(EXTRA_KEY_TITLE, getString(R.string.latest_movies))
            })
        }

        binding.tvMoreLatestSeries.setOnClickListener {
            startActivity(Intent(requireActivity(), ListActivity::class.java).apply {
                putExtra(EXTRA_KEY_MODE, LIST_LATEST_SERIES)
                putExtra(EXTRA_KEY_TITLE, getString(R.string.latest_series))
            })
        }

        binding.tvMoreLatestAnimations.setOnClickListener {
            startActivity(Intent(requireActivity(), ListActivity::class.java).apply {
                putExtra(EXTRA_KEY_MODE, LIST_LATEST_ANIMATIONS)
                putExtra(EXTRA_KEY_TITLE, getString(R.string.latest_animations))
            })
        }

        binding.tvMorePopularMovies.setOnClickListener {
            startActivity(Intent(requireActivity(), ListActivity::class.java).apply {
                putExtra(EXTRA_KEY_MODE, LIST_POPULAR_MOVIES)
                putExtra(EXTRA_KEY_TITLE, getString(R.string.popular_movies))
            })
        }

        binding.tvMorePopularSeries.setOnClickListener {
            startActivity(Intent(requireActivity(), ListActivity::class.java).apply {
                putExtra(EXTRA_KEY_MODE, LIST_POPULAR_SERIES)
                putExtra(EXTRA_KEY_TITLE, getString(R.string.popular_series))
            })
        }

        binding.tvMorePopularAnimations.setOnClickListener {
            startActivity(Intent(requireActivity(), ListActivity::class.java).apply {
                putExtra(EXTRA_KEY_MODE, LIST_POPULAR_ANIMATIONS)
                putExtra(EXTRA_KEY_TITLE, getString(R.string.popular_animations))
            })
        }

        binding.ivSearch.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }
    }

    val runnable = Runnable {
        if (binding.bannerSliderViewPager.currentItem < bannersCount!! - 1)
            binding.bannerSliderViewPager.currentItem =
                binding.bannerSliderViewPager.currentItem + 1
        else
            binding.bannerSliderViewPager.currentItem = 0
    }

    private fun setUpTransformer() {
        binding.bannerSliderViewPager.clipToPadding = false
        binding.bannerSliderViewPager.clipChildren = false
        binding.bannerSliderViewPager.offscreenPageLimit = 3
        binding.bannerSliderViewPager.getChildAt(0).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(20))
        binding.bannerSliderViewPager.setPageTransformer(transformer)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(movieadoEvent: MovieadoEvent) {
        when (movieadoEvent.type) {
            MovieadoEvent.Type.SIMPLE -> {
                val connectionView = showConnectionLost(true)
                connectionView?.findViewById<MaterialButton>(R.id.btnRetry)?.setOnClickListener {
                    showConnectionLost(false)
//                    viewModel.showAnimationList()
                }
            }
            else -> {}
        }
    }

    private fun snackBar(message: String) {
        Snackbar.make(binding.root as View, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onGenreClick(genre: Genre) {
        startActivity(Intent(requireActivity(), GenreActivity::class.java).apply {
            putExtra(EXTRA_KEY_TITLE, genre.title)
        })
    }

    override fun onMovieClicked(movie: Movie) {
        FirebaseAnalytics.getInstance(requireContext())
            .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, Bundle().apply {
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, movie.name)
                putString(FirebaseAnalytics.Param.ITEM_ID, movie.name)
            })

        if (movie.type == "ANIMATION_SERIES" || movie.type == "SERIES") {
            startActivity(Intent(requireActivity(), DetailSeriesActivity::class.java).apply {
                putExtra(EXTRA_KEY_ID, movie.id)
            })
        } else if (movie.type == "ANIMATION_MOVIE" || movie.type == "MOVIE") {
            startActivity(Intent(requireActivity(), DetailMovieActivity::class.java).apply {
                putExtra(EXTRA_KEY_ID, movie.id)
            })
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
        EventBus.getDefault().unregister(this)
        compositeDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 5000)
        FirebaseAnalytics.getInstance(requireContext())
            .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, "HomeFragment")
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.javaClass.simpleName)
            })
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}