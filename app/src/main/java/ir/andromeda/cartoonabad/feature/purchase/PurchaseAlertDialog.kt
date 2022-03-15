package ir.andromeda.cartoonabad.feature.purchase

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.databinding.DialogAlertPurchaseBinding

class PurchaseAlertDialog : DialogFragment() {

    private var _binding: DialogAlertPurchaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAlertPurchaseBinding.inflate(LayoutInflater.from(requireContext()))

        val builder = AlertDialog.Builder(context)

        binding.ivBack.setOnClickListener {
            dismiss()
        }

        binding.btnPurchase.setOnClickListener {
//            findNavController().navigate(R.id.action_purchaseAlertDialog_to_purchaseFragment)
            dismiss()
        }

        builder.setView(binding.root)
        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}