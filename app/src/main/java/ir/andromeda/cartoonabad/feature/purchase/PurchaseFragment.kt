package ir.andromeda.cartoonabad.feature.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.MONTHLY
import ir.andromeda.cartoonabad.common.THREE_MONTH
import ir.andromeda.cartoonabad.common.YEARLY
import ir.andromeda.cartoonabad.databinding.FragmentPurchaseBinding
import ir.andromeda.cartoonabad.feature.main.DrawerLocker

class PurchaseFragment : CartoonAbadFragment() {

    private var _binding: FragmentPurchaseBinding? = null
    private val binding get() = _binding!!

    private lateinit var lastSelectedLayout: View
    private var selectedPlan: Int = YEARLY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lastSelectedLayout = binding.layoutYearly

        binding.layoutYearly.setOnClickListener {
            lastSelectedLayout.background =  ContextCompat.getDrawable(requireContext(), R.drawable.shape_default_plan)
            lastSelectedLayout = it
            it.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_selected_plan)
            selectedPlan = YEARLY
        }

        binding.layoutThreeMonth.setOnClickListener {
            lastSelectedLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_default_plan)
           lastSelectedLayout = it
            it.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_selected_plan)
            selectedPlan = THREE_MONTH
        }

        binding.layoutMonthly.setOnClickListener {
            lastSelectedLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_default_plan)
            lastSelectedLayout = it
            it.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_selected_plan)
            selectedPlan = MONTHLY
        }

        (activity as DrawerLocker).setDrawerLocked(true)
    }

    override fun onStop() {
        super.onStop()
        (activity as DrawerLocker).setDrawerLocked(false)
    }
}