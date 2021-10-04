package ir.andromeda.cartoonabad.feature.purchase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.feature.main.DrawerLocker

class PurchaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_purchase, container, false)
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