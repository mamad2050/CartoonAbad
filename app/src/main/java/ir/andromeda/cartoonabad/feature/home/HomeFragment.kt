package ir.andromeda.cartoonabad.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.OnItemEventListener
import ir.andromeda.cartoonabad.data.PurchaseContainer
import ir.andromeda.cartoonabad.data.animation.Animation
import ir.andromeda.cartoonabad.databinding.FragmentHomeBinding
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.ConnectionState
import ir.cafebazaar.poolakey.Payment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : CartoonAbadFragment(), OnItemEventListener<Animation> {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var adapter: AnimationAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: HomeViewModel by viewModel()
    private val payment: Payment by inject()
    private lateinit var paymentConnection: Connection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkSubscription()

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        viewModel.animationsLiveData.observe(viewLifecycleOwner) {

            binding.rvAnimations.layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = AnimationAdapter(it, imageLoadingService, this)
            binding.rvAnimations.adapter = adapter

        }

    }

    private fun checkSubscription() {
        paymentConnection = payment.connect {
            connectionSucceed {
                payment.getSubscribedProducts {
                    querySucceed { purchasedItems ->
                        if (purchasedItems.isNotEmpty()) {
                            PurchaseContainer.setPurchaseInfo(purchasedItems[0])
                        }
                    }
                    queryFailed {

                    }
                }
            }
            connectionFailed {
                toast(getString(R.string.did_not_connect_to_bazaar))
            }
            disconnected {
            }
        }
    }

    override fun onStop() {
        super.onStop()
        _binding = null
        paymentConnection.disconnect()
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onCLick(item: Animation) {
        val action = HomeFragmentDirections.navigateToListFragment(item)
        Navigation.findNavController(requireView()).navigate(action)
    }

}