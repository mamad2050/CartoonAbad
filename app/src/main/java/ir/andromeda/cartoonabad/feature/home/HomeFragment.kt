package ir.andromeda.cartoonabad.feature.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.CompositeDisposable
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.EXTRA_KEY_ID
import ir.andromeda.cartoonabad.common.ZONE_ID_INTERSTITIAL_BANNER_AD
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.databinding.FragmentHomeBinding
import ir.andromeda.cartoonabad.feature.detail.DetailCartoonActivity
import ir.andromeda.cartoonabad.feature.detail.DetailSeriesActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : CartoonAbadFragment(),
    SeriesAdapter.OnSeriesItemEventListener, CartoonAdapter.OnCartoonItemEventListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var genresAdapter: GenreAdapter
    private lateinit var latestSeriesAdapter: SeriesAdapter
    private lateinit var latestCartoonsAdapter: CartoonAdapter
    private lateinit var popularSeriesAdapter: SeriesAdapter
    private lateinit var popularCartoonsAdapter: CartoonAdapter
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: HomeViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    private var adResponseId: String? = null
    private lateinit var handler: Handler
    var currentIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }


        viewModel.bannersLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            val bannerSlideAdapter = BannerSliderAdapter(this, it)
            binding.bannerSliderViewPager.adapter = bannerSlideAdapter
            binding.sliderIndicator.setViewPager2(binding.bannerSliderViewPager)

            handler = Handler(Looper.getMainLooper()!!)

            setUpTransformer()
//
//            binding.bannerSliderViewPager.clipToPadding = false
//            binding.bannerSliderViewPager.clipChildren = false
//            binding.bannerSliderViewPager.offscreenPageLimit = 3
//            binding.bannerSliderViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER;
//
//

            binding.bannerSliderViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    handler.postDelayed(runnable, 5000)
                    handler.removeCallbacks(runnable)

                }

            })
        }

        viewModel.genresLiveData.observe(viewLifecycleOwner) {
            binding.rvGenres.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            genresAdapter = GenreAdapter(it as ArrayList<Genre>, imageLoadingService)
            binding.rvGenres.adapter = genresAdapter
        }

        viewModel.latestSeriesLiveData.observe(viewLifecycleOwner) {
            binding.rvLatestSeries.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            latestSeriesAdapter = SeriesAdapter(it, this, imageLoadingService)
            binding.rvLatestSeries.adapter = latestSeriesAdapter
        }

        viewModel.latestCartoonsLiveData.observe(viewLifecycleOwner) {
            binding.rvLatestCartoons.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            latestCartoonsAdapter =
                CartoonAdapter(it as ArrayList<Cartoon>, this, imageLoadingService)
            binding.rvLatestCartoons.adapter = latestCartoonsAdapter

        }

        viewModel.popularSeriesLiveData.observe(viewLifecycleOwner) {
            binding.rvPopularSeries.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularSeriesAdapter = SeriesAdapter(it, this, imageLoadingService)
            binding.rvPopularSeries.adapter = popularSeriesAdapter
        }

        viewModel.popularCartoonsLiveData.observe(viewLifecycleOwner) {
            binding.rvPopularCartoons.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularCartoonsAdapter = CartoonAdapter(it, this, imageLoadingService)
            binding.rvPopularCartoons.adapter = popularCartoonsAdapter
        }

    }

    private val runnable = Runnable {
        binding.bannerSliderViewPager.currentItem = binding.bannerSliderViewPager.currentItem + 1
    }

    private fun setUpTransformer() {

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        binding.bannerSliderViewPager.setPageTransformer(transformer)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(cartoonAbadEvent: CartoonAbadEvent) {
        when (cartoonAbadEvent.type) {
            CartoonAbadEvent.Type.SIMPLE -> {
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
        Snackbar.make(
            activity?.findViewById(R.id.contentRootView) as View, message, Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        _binding = null
        EventBus.getDefault().unregister(this)
        compositeDisposable.dispose()
    }

    private fun requestBannerAd() {
        TapsellPlus.requestInterstitialAd(
            requireActivity(),
            ZONE_ID_INTERSTITIAL_BANNER_AD,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    adResponseId = tapsellPlusAdModel.responseId
                    showBannerAd()
                }

                override fun error(message: String?) {}
            })
    }

    private fun showBannerAd() {

        TapsellPlus.showInterstitialAd(requireActivity(), adResponseId,
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                }

                override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onClosed(tapsellPlusAdModel)
                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    super.onError(tapsellPlusErrorModel)
                }
            })
    }

//    override fun onCLick(item: Animation) {
//
//        FirebaseAnalytics.getInstance(requireContext())
//            .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, Bundle().apply {
//                putString(FirebaseAnalytics.Param.CONTENT_TYPE, item.name)
//                putString(FirebaseAnalytics.Param.ITEM_ID, item.name)
//            })
//
////        if (item.no_episodes.toInt() > 0) {
////
////            startActivity(Intent(requireActivity(), DetailSeriesActivity::class.java).apply {
////                putExtra(EXTRA_KEY_DATA, item)
////            })
////
////        } else {
////            Toast.makeText(requireContext(), "به زودی ...", Toast.LENGTH_SHORT).show()
////        }
//    }

//    private fun showUpdateDialog() {
//        viewModel.getVersionNumber()
//            .asyncNetworkRequest()
//            .subscribe(object : CartoonAbadSingleObserver<AppData>(compositeDisposable) {
//                override fun onSuccess(t: AppData) {
//                    if (t.version.toInt() > BuildConfig.VERSION_CODE)
//                        findNavController().navigate(R.id.navigateToUpdateAlertDialog)
//                }
//            })
//    }

    override fun onResume() {
        super.onResume()


        FirebaseAnalytics.getInstance(requireContext())
            .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, "HomeFragment")
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.javaClass.simpleName)
            })
    }


    override fun clickOnSeries(series: Series) {
        startActivity(Intent(requireActivity(), DetailSeriesActivity::class.java).apply {
            putExtra(EXTRA_KEY_ID, series.id)
        })
        requestBannerAd()
    }

    override fun clickOnCartoon(cartoon: Cartoon) {
        startActivity(Intent(requireActivity(), DetailCartoonActivity::class.java).apply {
            putExtra(EXTRA_KEY_ID, cartoon.id)
        })
        requestBannerAd()
    }


//    private fun setAnimationForSlider() {
//        val paddingPx = 180
//        val MIN_SCALE = 0.8f
//        val MAX_SCALE = 1f
//        binding.bannerSliderViewPager.setClipToPadding(false)
//        binding.bannerSliderViewPager.setPadding(paddingPx, 0, paddingPx, 0)
//        val transformer = ViewPager2.PageTransformer { page: View, position: Float ->
//            val pagerWidthPx = (page.parent as ViewPager).width.toFloat()
//            val pageWidthPx = pagerWidthPx - 2 * paddingPx
//            val maxVisiblePages = pagerWidthPx / pageWidthPx
//            val center = maxVisiblePages / 2f
//            val scale: Float
//            if (position + 0.5f < center - 0.5f || position > center) {
//                scale = MIN_SCALE
//            } else {
//                val coef: Float
//                coef = if (position + 0.5f < center) {
//                    (position + 1 - center) / 0.5f
//                } else {
//                    (center - position) / 0.5f
//                }
//                scale = coef * (MAX_SCALE - MIN_SCALE) + MIN_SCALE
//            }
//            page.scaleX = scale
//            page.scaleY = scale
//        }
//        binding.bannerSliderViewPager.setPageTransformer( transformer)
//    }
}