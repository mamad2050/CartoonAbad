package ir.andromeda.cartoonabad.feature.update

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import ir.andromeda.cartoonabad.databinding.DialogAlertUpdateBinding
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import ir.andromeda.cartoonabad.BuildConfig
import ir.andromeda.cartoonabad.R

class UpdateAlertDialog : DialogFragment() {

    private var _binding: DialogAlertUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAlertUpdateBinding.inflate(LayoutInflater.from(requireContext()))

        val builder = AlertDialog.Builder(context)

        binding.btnUpdate.setOnClickListener {
            goToBazaar()
        }

        isCancelable = false

        builder.setView(binding.root)
        return builder.create()
    }

    private fun goToBazaar() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("bazaar://details?id=${BuildConfig.APPLICATION_ID}")
            intent.setPackage("com.farsitel.bazaar")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.install_bazaar_app),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}