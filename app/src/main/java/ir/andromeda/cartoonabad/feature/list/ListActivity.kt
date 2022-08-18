package ir.andromeda.cartoonabad.feature.list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import timber.log.Timber

class ListActivity : CartoonAbadActivity(), SeriesAdapter.OnSeriesItemEventListener,
    CartoonAdapter.OnCartoonItemEventListener {

    private lateinit var binding: ActivityListBinding
    private val viewModel: ListViewModel by viewModel { parametersOf(intent.extras) }
    private lateinit var cartoonAdapter: CartoonAdapter
    private val seriesAdapter = SeriesAdapter(this, get(), ItemScale.LARGE)
    var currentPage = 1
    var hasNextPage = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvList.layoutManager = GridLayoutManager(this, 3)
        binding.tvTitleToolbar.text = intent.getStringExtra(TITLE)

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        when (intent.getStringExtra(MODE)) {

            LATEST_SERIES, MOST_VIEWED_SERIES -> {
                binding.rvList.adapter = seriesAdapter

                viewModel.seriesLiveData.observe(this) {

                    if (it.isEmpty()){
                        hasNextPage = false
                    }

                    if (currentPage == 1)
                        seriesAdapter.setData(it)
                    else {
                        seriesAdapter.addNewData(it)
                        binding.rvList.smoothScrollToPosition(15)
                    }

                }
            }

            LATEST_CARTOONS, MOST_VIEWED_CARTOONS -> {

                viewModel.cartoonLiveData.observe(this) {
                    cartoonAdapter = CartoonAdapter(it, this, get(), ItemScale.LARGE)
                    binding.rvList.adapter = cartoonAdapter
                }
            }
        }

        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (hasNextPage) {
                        viewModel.getLatestSeries(++currentPage)
                    }
                }
            }
        })
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