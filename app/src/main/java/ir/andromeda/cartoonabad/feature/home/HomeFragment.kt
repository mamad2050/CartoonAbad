package ir.andromeda.cartoonabad.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.common.OnItemEventListener
import ir.andromeda.cartoonabad.common.ZONE_ID_INTERSTITIAL_BANNER_AD
import ir.andromeda.cartoonabad.common.ZONE_ID_INTERSTITIAL_VIDEO_AD
import ir.andromeda.cartoonabad.data.animation.Animation
import ir.andromeda.cartoonabad.databinding.FragmentHomeBinding
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

class HomeFragment : CartoonAbadFragment(), OnItemEventListener<Animation> {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var adapter: AnimationAdapter? = null
    private var adResponseId: String? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: HomeViewModel by viewModel()


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

        viewModel.animationsLiveData.observe(viewLifecycleOwner) {

            binding.rvAnimations.layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = AnimationAdapter(it, imageLoadingService, this)
            binding.rvAnimations.adapter = adapter
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(cartoonAbadEvent: CartoonAbadEvent) {
        when (cartoonAbadEvent.type) {
            CartoonAbadEvent.Type.SIMPLE -> snackBar(
                cartoonAbadEvent.stringMessage
                    ?: getString(cartoonAbadEvent.resMessage)
            )
        }

    }

    private fun snackBar(message: String) {
        Snackbar.make(
            activity?.findViewById(R.id.contentRootView) as View, message, Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onStart() {
        super.onStart()
        requestAd()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        _binding = null
        EventBus.getDefault().unregister(this)
    }

    override fun onCLick(item: Animation) {
        showAd()
        val action = HomeFragmentDirections.navigateToListFragment(item)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun requestAd() {
        TapsellPlus.requestInterstitialAd(
            activity,
            ZONE_ID_INTERSTITIAL_BANNER_AD,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    adResponseId = tapsellPlusAdModel.responseId
                }

                override fun error(message: String?) {}
            })
    }

    private fun showAd() {

        TapsellPlus.showInterstitialAd(activity, adResponseId,
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

}