package ir.andromeda.cartoonabad.feature.dwnloaded

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ir.andromeda.cartoonabad.R

import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.data.downloaded.Downloaded
import ir.andromeda.cartoonabad.databinding.FragmentDownloadedBinding
import ir.andromeda.cartoonabad.feature.main.DrawerLocker
import ir.andromeda.cartoonabad.feature.player.PlayerActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class DownloadedFragment : CartoonAbadFragment(), DownloadedAdapter.EpisodeEventListener {

    private var _binding: FragmentDownloadedBinding? = null
    private val binding  get() =  _binding!!
    private var adapter: DownloadedAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: DownloadedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDownloadedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as DrawerLocker).setDrawerLocked(true)

        viewModel.downloadedEpisodeLiveData.observe(viewLifecycleOwner){
            if (it.isNotEmpty()) {
                binding.rvDownloads.layoutManager = LinearLayoutManager(requireContext())
                adapter = DownloadedAdapter(it as ArrayList, imageLoadingService, this)
                binding.rvDownloads.adapter = adapter
                Timber.i(it.toString())
            }

        }

        viewModel.emptyStateLiveData.observe(viewLifecycleOwner) { emptyState ->
            if (emptyState.mustShow) {
                showEmptyState(R.layout.view_download_empty_state)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as DrawerLocker).setDrawerLocked(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRemoveClick(episode: Downloaded) {
        viewModel.removeFromDownloads(episode)
    }

    override fun onEpisodeClick(episode: Downloaded) {
        startActivity(Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, episode)
        })
    }
}