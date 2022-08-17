package ir.andromeda.cartoonabad.feature.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import ir.andromeda.cartoonabad.common.CartoonAbadActivity
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries
import ir.andromeda.cartoonabad.databinding.ActivitySearchBinding
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class SearchActivity : CartoonAbadActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by inject()
    private val adapter = SearchAdapter(get())

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

                if (word!!.isEmpty())
                    adapter.clear()
                else
                    viewModel.search(word.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        viewModel.searchLiveData.observe(this) {
            adapter.updateData(it)
        }
    }
}