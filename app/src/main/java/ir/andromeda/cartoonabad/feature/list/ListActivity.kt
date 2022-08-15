package ir.andromeda.cartoonabad.feature.list

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.databinding.ActivityListBinding
import ir.andromeda.cartoonabad.feature.detail.DetailCartoonActivity
import ir.andromeda.cartoonabad.feature.detail.DetailSeriesActivity
import ir.andromeda.cartoonabad.feature.home.CartoonAdapter
import ir.andromeda.cartoonabad.feature.home.SeriesAdapter
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ListActivity : CartoonAbadActivity(), SeriesAdapter.OnSeriesItemEventListener,
    CartoonAdapter.OnCartoonItemEventListener {

    private lateinit var binding: ActivityListBinding
    private val viewModel: ListViewModel by viewModel { parametersOf(intent.extras) }
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var cartoonAdapter: CartoonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvList.layoutManager = GridLayoutManager(this, 3)
        binding.tvTitleToolbar.text = intent.getStringExtra(TITLE)

        when (intent.getStringExtra(MODE)) {
            LATEST_SERIES, MOST_VIEWED_SERIES -> {
                viewModel.seriesLiveData.observe(this) {
                    seriesAdapter = SeriesAdapter(it, this, get(), ItemScale.LARGE)
                    binding.rvList.adapter = seriesAdapter
                }
            }

            LATEST_CARTOONS, MOST_VIEWED_CARTOONS -> {
                viewModel.cartoonLiveData.observe(this) {
                    cartoonAdapter = CartoonAdapter(it, this, get(), ItemScale.LARGE)
                    binding.rvList.adapter = cartoonAdapter
                }
            }
        }
    }

    override fun clickOnSeries(series: Series) {
        startActivity(Intent(this, DetailSeriesActivity::class.java).apply {
            putExtra(EXTRA_KEY_ID, series.id)
        })
    }

    override fun clickOnCartoon(cartoon: Cartoon) {
        startActivity(Intent(this, DetailCartoonActivity::class.java).apply {
            putExtra(EXTRA_KEY_ID, cartoon.id)
        })
    }
}