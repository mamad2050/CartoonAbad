package ir.andromeda.cartoonabad.feature.search

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.databinding.FragmentFilterBottomSheetBinding
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterBottomSheetDialog(
    val listener: OnFilterListener
) : BottomSheetDialogFragment(),
    GenreFilterAdapter.OnGenreEventListener {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!
    val viewModel: FilterFragmentViewModel by viewModel()
    var sortBy: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog

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

        binding.chipMostViewed.typeface = binding.btnApply.typeface
        binding.chipLatest.typeface = binding.btnApply.typeface
        binding.chipImdb.typeface = binding.btnApply.typeface

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                binding.chipImdb.id -> sortBy = SORT_BY_IMDB
                binding.chipMostViewed.id -> sortBy = SORT_BY_VIEW
                binding.chipLatest.id -> sortBy = SORT_BY_LATEST
            }
        }

        binding.btnApply.setOnClickListener {
            listener.onApplyFilter(sortBy, null)
            dismiss()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onGenreClick(genre: Genre) {

    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        bottomSheet.layoutParams = layoutParams
    }

    interface OnFilterListener {
        fun onApplyFilter(sortBy: String?, selectedGenres: List<Genre>?)
    }
}