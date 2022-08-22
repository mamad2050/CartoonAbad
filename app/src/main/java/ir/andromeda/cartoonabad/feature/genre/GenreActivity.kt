package ir.andromeda.cartoonabad.feature.genre

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.combined.CombinedCartoonSeries
import ir.andromeda.cartoonabad.databinding.ActivityGenreBinding
import ir.andromeda.cartoonabad.feature.detail.DetailCartoonActivity
import ir.andromeda.cartoonabad.feature.detail.DetailSeriesActivity
import ir.andromeda.cartoonabad.feature.CombinedCartoonSeriesAdapter
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GenreActivity : CartoonAbadActivity(), OnItemEventListener<CombinedCartoonSeries> {

    private lateinit var binding: ActivityGenreBinding
    private val viewModel: GenreViewModel by viewModel { parametersOf(intent.extras) }
    private var adapter = CombinedCartoonSeriesAdapter(get(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvData.layoutManager = GridLayoutManager(this, 3)


        binding.tvTitleToolbar.text = intent.getStringExtra(TITLE)

            viewModel.progressBarLiveData.observe(this) {
                setProgressIndicator(it)
            }

        viewModel.genresLiveData.observe(this) {
            adapter.setData(it as ArrayList<CombinedCartoonSeries>)
            binding.rvData.adapter = adapter
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
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