package ir.andromeda.cartoonabad.feature.purchase

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.PurchaseContainer
import ir.andromeda.cartoonabad.databinding.FragmentPurchaseBinding
import ir.andromeda.cartoonabad.feature.main.DrawerLocker
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.ConnectionState
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.request.PurchaseRequest
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PurchaseFragment : CartoonAbadFragment() {

    private var _binding: FragmentPurchaseBinding? = null
    private val binding get() = _binding!!

    private lateinit var lastSelectedLayout: View
    private var selectedPlan: Int = YEARLY

    private val payment: Payment by inject()
    private lateinit var paymentConnection: Connection

    private val viewModel : PurchaseViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        startPaymentConnection()

        viewModel.progressBarLiveData.observe(viewLifecycleOwner){
            setProgressIndicator(it)
        }

        viewModel.subscriptionsLiveData.observe(viewLifecycleOwner){

            binding.txtDurationMonthly.text = it[0].duration
            binding.txtPriceMonthly.text = formatPrice(it[0].price)

            binding.txtDurationSeasonly.text = it[1].duration
            binding.txtPriceSeasonly.text = formatPrice(it[1].price)

            binding.txtDurationYearly.text = it[2].duration
            binding.txtPriceYearly.text = formatPrice(it[2].price)

            customButtons()

        }

        binding.btnPurchase.setOnClickListener {
            if (PurchaseContainer.purchaseInfo == null) {
                if (paymentConnection.getState() == ConnectionState.Connected) {

                    val purchaseId = when (selectedPlan) {
                        YEARLY -> "rearkneth645thewth56t"
                        THREE_MONTH -> "erigneotrht6h465ryjry"
                        MONTHLY -> "fdgnsgmsrymsryr6t516rt"
                        else -> "rearkneth645thewth56t"
                    }

                    subscribeProduct(purchaseId)
                }
            } else {
                snackBar(getString(R.string.you_have_subscription))
            }
        }

        (activity as DrawerLocker).setDrawerLocked(true)
    }

    private fun customButtons() {
        lastSelectedLayout = binding.layoutYearly

        binding.layoutYearly.setOnClickListener {
            lastSelectedLayout.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_default_plan)
            lastSelectedLayout = it
            it.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_selected_plan)
            selectedPlan = YEARLY
        }

        binding.layoutThreeMonth.setOnClickListener {
            lastSelectedLayout.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_default_plan)
            lastSelectedLayout = it
            it.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_selected_plan)
            selectedPlan = THREE_MONTH
        }

        binding.layoutMonthly.setOnClickListener {
            lastSelectedLayout.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_default_plan)
            lastSelectedLayout = it
            it.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_selected_plan)
            selectedPlan = MONTHLY
        }
    }

    private fun startPaymentConnection() {
        paymentConnection = payment.connect {
            connectionSucceed {
            }
            connectionFailed {
                snackBar(getString(R.string.install_bazaar_app))
            }
            disconnected {
            }
        }
    }

    private fun snackBar(message: String) {
        Snackbar.make(
            activity?.findViewById(R.id.contentRootView) as View, message, Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun subscribeProduct(purchaseId: String) {
        payment.subscribeProduct(
            this@PurchaseFragment,
            request = PurchaseRequest(
                productId = purchaseId,
                requestCode = 1000,
                payload = ""
            )
        ) {
            purchaseFlowBegan {

            }
            failedToBeginFlow {
                snackBar(getString(R.string.purchase_failed))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        payment.onActivityResult(requestCode, resultCode, data) {
            purchaseSucceed {
                snackBar(getString(R.string.success_purchase))
            }
            purchaseCanceled {

            }
            purchaseFailed {
                snackBar(getString(R.string.purchase_failed))
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        (activity as DrawerLocker).setDrawerLocked(false)
        paymentConnection.disconnect()
    }
}