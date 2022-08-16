package ir.andromeda.cartoonabad.feature.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.GridLayoutManager
import ir.andromeda.cartoonabad.common.CartoonAbadActivity
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries
import ir.andromeda.cartoonabad.databinding.ActivitySearchBinding
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class SearchActivity : CartoonAbadActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by inject()
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvResult.layoutManager = GridLayoutManager(this, 3)

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(word: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.search(word.toString())
                if (word != null) {
                    if (word.isEmpty()){
                     adapter.clear()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        viewModel.searchLiveData.observe(this) {
            adapter = SearchAdapter(it as ArrayList<CombinedCartoonSeries>, get())
            binding.rvResult.adapter = adapter
        }
    }
}