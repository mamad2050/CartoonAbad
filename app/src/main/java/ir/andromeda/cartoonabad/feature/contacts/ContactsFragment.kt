package ir.andromeda.cartoonabad.feature.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.databinding.FragmentContactsBinding
import ir.andromeda.cartoonabad.feature.main.DrawerLocker
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsFragment : CartoonAbadFragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as DrawerLocker).setDrawerLocked(true)

    }

    override fun onStop() {
        super.onStop()
        (activity as DrawerLocker).setDrawerLocked(false)
    }

}