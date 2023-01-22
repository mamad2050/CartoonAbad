package ir.andromeda.movieado.feature.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import ir.andromeda.movieado.R
import ir.andromeda.movieado.common.MovieadoFragment
import ir.andromeda.movieado.databinding.FragmentAccountBinding

class AccountFragment : MovieadoFragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSubscription.setOnClickListener {
            it.findNavController().navigate(R.id.action_accountFragment_to_subscriptionFragment)
        }

        binding.btnRate.setOnClickListener {

        }

        binding.btnContacts.setOnClickListener {
            it.findNavController().navigate(R.id.action_accountFragment_to_contactsFragment)
        }

        binding.btnShare.setOnClickListener {

        }

        binding.btnExit.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()

        FirebaseAnalytics.getInstance(requireContext())
            .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, "AccountFragment")
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.javaClass.simpleName)
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}