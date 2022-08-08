package ir.andromeda.cartoonabad.feature.purchase

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.PurchaseContainer
import ir.andromeda.cartoonabad.databinding.FragmentPurchaseBinding
import ir.andromeda.cartoonabad.feature.main.MainActivity
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

    private val viewModel: PurchaseViewModel by viewModel()

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

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        viewModel.subscriptionsLiveData.observe(viewLifecycleOwner) {

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
                        YEARLY -> YEARLY_PRODUCT_ID
                        THREE_MONTH -> THREE_MONTH_PRODUCT_ID
                        MONTHLY -> MONTHLY_PRODUCT_ID
                        else -> YEARLY_PRODUCT_ID
                    }

                    subscribeProduct(purchaseId)
                }
            } else {
                snackBar(getString(R.string.you_have_subscription))
            }
        }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(cartoonAbadEvent: CartoonAbadEvent) {
        when (cartoonAbadEvent.type) {
            CartoonAbadEvent.Type.SIMPLE -> {
                val connectionView = showConnectionLost(true)
                connectionView?.findViewById<MaterialButton>(R.id.btnRetry)?.setOnClickListener {
                    showConnectionLost(false)
                    viewModel.showSubscriptionPrices()
                }
            }
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
            binding.root, message, Snackbar.LENGTH_SHORT
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
                PurchaseContainer.setPurchaseInfo(it)
                //set subscription day
                (requireActivity() as MainActivity).setSubscriptionDays(
                    it.purchaseTime,
                    it.productId
                )
                snackBar(getString(R.string.success_purchase))

                val plan = when (selectedPlan) {
                    YEARLY -> "YEARLY"
                    THREE_MONTH -> "THREE_MONTH"
                    MONTHLY -> "MONTHLY"
                    else -> "YEARLY"
                }
                FirebaseAnalytics.getInstance(requireContext())
                    .logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, Bundle().apply {
                        putString(FirebaseAnalytics.Param.PAYMENT_TYPE, plan)
                    })

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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        paymentConnection.disconnect()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()


        FirebaseAnalytics.getInstance(requireContext())
            .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, "PurchaseFragment")
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.javaClass.simpleName)
            })
    }
}