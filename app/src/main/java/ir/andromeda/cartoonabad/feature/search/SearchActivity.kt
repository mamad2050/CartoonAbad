package ir.andromeda.cartoonabad.feature.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.GridLayoutManager
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries
import ir.andromeda.cartoonabad.databinding.ActivitySearchBinding
import ir.andromeda.cartoonabad.feature.detail.DetailCartoonActivity
import ir.andromeda.cartoonabad.feature.detail.DetailSeriesActivity
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : CartoonAbadActivity(), OnItemEventListener<CombinedCartoonSeries> {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel()
    private val adapter = CombinedCartoonSeriesAdapter(get(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvResult.layoutManager = GridLayoutManager(this, 3)
        binding.rvResult.adapter = adapter

        viewModel.progressBarLiveData.observe(this) {
            if (it) {
                binding.rvResult.visibility = View.GONE
                binding.spinkit.visibility = View.VISIBLE
            } else {
                binding.rvResult.visibility = View.VISIBLE
                binding.spinkit.visibility = View.GONE
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(word: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (word!!.isEmpty()) {
                    adapter.clear()
                    binding.layoutSearchNotFound.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(v.text.toString())
            }
            false
        }

        viewModel.searchLiveData.observe(this) {
            if (it.isEmpty()) {
                binding.layoutSearchNotFound.visibility = View.GONE
            } else {
                adapter.setData(it as ArrayList<CombinedCartoonSeries>)
                binding.rvResult.visibility = View.VISIBLE
            }
        }


        viewModel.showNotFoundState.observe(this) {
            if (it) {
                binding.layoutSearchNotFound.visibility = View.VISIBLE
            } else {
                binding.layoutSearchNotFound.visibility = View.GONE
            }
        }
    }

    override fun clickOnItem(item: CombinedCartoonSeries) {
        if (item.type == CARTOON) {
            startActivity(Intent(this, DetailCartoonActivity::class.java).apply {
                putExtra(EXTRA_KEY_ID, item.id)
            })
        }
        if (item.type == SERIES) {
            startActivity(Intent(this, DetailSeriesActivity::class.java).apply {
                putExtra(EXTRA_KEY_ID, item.id)
            })
        }
    }
}