package ir.andromeda.cartoonabad.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.databinding.FragmentHomeBinding
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : CartoonAbadFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var adapter: AnimationAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: HomeViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }



        viewModel.animationsLiveData.observe(viewLifecycleOwner) {

            binding.rvAnimations.layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = AnimationAdapter(it,imageLoadingService)
            binding.rvAnimations.adapter = adapter

           Toast.makeText(requireContext(),it[0].name,Toast.LENGTH_SHORT).show()

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}