package ir.andromeda.cartoonabad.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.CompositeDisposable
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.databinding.FragmentHomeBinding
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : CartoonAbadFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var latestSeriesAdapter: SeriesAdapter
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: HomeViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

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

//        showUpdateDialog()
        viewModel.genresLiveData.observe(viewLifecycleOwner) {
            binding.rvGenres.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            genreAdapter = GenreAdapter(it as ArrayList<Genre>, imageLoadingService)
            binding.rvGenres.adapter = genreAdapter
        }

        viewModel.latestSeriesLiveData.observe(viewLifecycleOwner) {
            binding.rvLatestSeries.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            latestSeriesAdapter = SeriesAdapter(it, imageLoadingService)
            binding.rvLatestSeries.adapter = latestSeriesAdapter
        }


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
}