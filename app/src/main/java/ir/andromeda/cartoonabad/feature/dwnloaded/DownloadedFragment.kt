package ir.andromeda.cartoonabad.feature.dwnloaded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.databinding.FragmentDownloadedBinding
import ir.andromeda.cartoonabad.databinding.FragmentFavoriteBinding
import ir.andromeda.cartoonabad.feature.favorite.FavoriteAdapter
import ir.andromeda.cartoonabad.feature.favorite.FavoriteViewModel
import ir.andromeda.cartoonabad.feature.main.DrawerLocker
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.Error

class DownloadedFragment() : CartoonAbadFragment() {

    private var _binding: FragmentDownloadedBinding? = null
    private val binding  get() =  _binding!!
    private var adapter: DownloadedAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDownloadedBinding.inflate(inflater, container, false)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}