package ir.andromeda.cartoonabad.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.databinding.FragmentFilterBottomSheetBinding
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterBottomSheetDialog : BottomSheetDialogFragment(),
    GenreFilterAdapter.OnGenreEventListener {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!
    val viewModel: FilterFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.rvGenres.layoutManager =
            GridLayoutManager(
                requireContext(),
                3,
                GridLayoutManager.HORIZONTAL,
                false
            )

        viewModel.genresLiveData.observe(viewLifecycleOwner) {
            val genreFilterAdapter = GenreFilterAdapter(it as ArrayList<Genre>, get(), this)
            binding.rvGenres.adapter = genreFilterAdapter
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onGenreClick(genre: Genre) {

    }
}