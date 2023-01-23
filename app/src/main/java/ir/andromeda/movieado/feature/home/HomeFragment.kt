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
    GenreAdapter.GenreEventListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val genresAdapter by lazy {
        GenreAdapter(imageLoadingService, this)
    }
    private val latestMovieAdapter by lazy {
        MovieAdapter(this, imageLoadingService, ItemScale.SMALL)
    }
    private val latestSeriesAdapter by lazy {
        MovieAdapter(this, imageLoadingService, ItemScale.SMALL)
    }
    private val latestAnimationsAdapter by lazy {
        MovieAdapter(this, imageLoadingService, ItemScale.SMALL, true)
    }
    private val popularMovieAdapter by lazy {
        MovieAdapter(this, imageLoadingService, ItemScale.SMALL)
    }
    private val popularSeriesAdapter by lazy {
        MovieAdapter(this, imageLoadingService, ItemScale.SMALL)
    }
    private val popularAnimationsAdapter by lazy {
        MovieAdapter(this, imageLoadingService, ItemScale.SMALL, true)
    }

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

        //Genre adapter initialization
        binding.rvGenres.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvGenres.adapter = genresAdapter
        viewModel.genresLiveData.observe(viewLifecycleOwner) {
            genresAdapter.genres = it as ArrayList<Genre>
        }

        //Latest movies adapter initialization
        binding.rvLatestMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvLatestMovies.adapter = latestMovieAdapter
        viewModel.latestMoviesLiveData.observe(viewLifecycleOwner) {
            latestMovieAdapter.movieList = it as ArrayList<Movie>
        }

        //Latest series adapter initialization
        binding.rvLatestSeries.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvLatestSeries.adapter = latestSeriesAdapter
        viewModel.latestSeriesLiveData.observe(viewLifecycleOwner) {
            latestSeriesAdapter.movieList = it as ArrayList<Movie>
        }

        //Latest animations adapter initialization
        binding.rvLatestAnimations.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvLatestAnimations.adapter = latestAnimationsAdapter
        viewModel.latestAnimationsLiveData.observe(viewLifecycleOwner) {
            latestAnimationsAdapter.movieList = it as ArrayList<Movie>
        }

        //popular movies adapter initialization
        binding.rvPopularMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopularMovies.adapter = popularMovieAdapter
        viewModel.popularMoviesLiveData.observe(viewLifecycleOwner) {
            popularMovieAdapter.movieList = it as ArrayList<Movie>
        }

        //popular series adapter initialization
        binding.rvPopularSeries.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopularSeries.adapter = popularSeriesAdapter
        viewModel.popularSeriesLiveData.observe(viewLifecycleOwner) {
            popularSeriesAdapter.movieList = it as ArrayList<Movie>
        }

        //popular animations adapter initialization
        binding.rvPopularAnimations.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopularAnimations.adapter = popularAnimationsAdapter
        viewModel.popularAnimationsLiveData.observe(viewLifecycleOwner) {
            popularAnimationsAdapter.movieList = it as ArrayList<Movie>
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
                    setProgressIndicator(true)
//                    viewModel.showAnimationList()
                }
            }
            else -> {}
        }
    }

    private fun snackBar(message: String) {
        Snackbar.make(binding.root as View, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onGenreClicked(genre: Genre) {
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