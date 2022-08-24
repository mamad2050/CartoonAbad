package ir.andromeda.cartoonabad.feature.list

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.data.genre.Genre
import ir.andromeda.cartoonabad.data.series.Series
import ir.andromeda.cartoonabad.databinding.ActivityListBinding
import ir.andromeda.cartoonabad.feature.detail.DetailCartoonActivity
import ir.andromeda.cartoonabad.feature.detail.DetailSeriesActivity
import ir.andromeda.cartoonabad.feature.home.CartoonAdapter
import ir.andromeda.cartoonabad.feature.home.SeriesAdapter
import ir.andromeda.cartoonabad.feature.search.FilterBottomSheetDialog
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ListActivity : CartoonAbadActivity(), SeriesAdapter.OnSeriesItemEventListener,
    CartoonAdapter.OnCartoonItemEventListener, FilterBottomSheetDialog.OnFilterListener {

    private lateinit var binding: ActivityListBinding
    private val viewModel: ListViewModel by viewModel { parametersOf(intent.extras) }
    private val seriesAdapter = SeriesAdapter(this, get(), ItemScale.LARGE)
    private val cartoonAdapter = CartoonAdapter(this, get(), ItemScale.LARGE)
    private val filterBottomSheet = FilterBottomSheetDialog(this)
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

                    if (it.isEmpty()) {
                        hasNextPage = false
                    }

                    if (currentPage == 1) {
                        seriesAdapter.setData(it)
                    } else {
                        seriesAdapter.addNewData(it)
                        binding.rvList.smoothScrollToPosition(seriesAdapter.itemCount - 1)
                    }

                }
            }

            LATEST_CARTOONS, MOST_VIEWED_CARTOONS -> {

                binding.rvList.adapter = cartoonAdapter

                viewModel.cartoonLiveData.observe(this) {

                    if (it.isEmpty()) {
                        hasNextPage = false
                    }

                    if (currentPage == 1) {
                        cartoonAdapter.setData(it)
                    } else {
                        cartoonAdapter.addNewData(it)
                        binding.rvList.smoothScrollToPosition(cartoonAdapter.itemCount - 1)
                    }
                }
            }
        }

        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (hasNextPage) {
                        when (intent.getStringExtra(MODE)) {
                            LATEST_SERIES -> viewModel.getLatestSeries(++currentPage)
                            LATEST_CARTOONS -> viewModel.getLatestCartoons(++currentPage)
                            MOST_VIEWED_SERIES -> viewModel.getMostViewedSeries(++currentPage)
                            MOST_VIEWED_CARTOONS -> viewModel.getMostViewedCartoons(++currentPage)
                        }
                    }
                }
            }
        })

        binding.fabFilter.setOnClickListener {
            showFilterBottomSheet()
        }
    }

    override fun onSeriesClick(series: Series) {
        startActivity(Intent(this, DetailSeriesActivity::class.java).apply {
            putExtra(EXTRA_KEY_ID, series.id)
        })
    }

    override fun onCartoonClick(cartoon: Cartoon) {
        startActivity(Intent(this, DetailCartoonActivity::class.java).apply {
            putExtra(EXTRA_KEY_ID, cartoon.id)
        })
    }


    private fun showFilterBottomSheet() {
        filterBottomSheet.show(supportFragmentManager, FILTER_FRAGMENT_TAG)
    }

    override fun onApplyFilter(sortBy: String?, selectedGenres: List<Genre>?) {
        seriesAdapter.filterData(sortBy!!)
    }

}