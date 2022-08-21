package ir.andromeda.cartoonabad.feature.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.data.download.Download
import ir.andromeda.cartoonabad.databinding.FragmentDownloadBinding
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class DownloadFragment : CartoonAbadFragment(), DownloadAdapter.EpisodeEventListener {

    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!
    private var adapter: DownloadAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: DownloadViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(requireContext(), "Download", Toast.LENGTH_SHORT).show()

        viewModel.downloadEpisodeLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                it.forEach { downloaded ->
                    val file = File(downloaded.path)
                    if (!file.exists()) {
                        it.remove(downloaded)
                        viewModel.removeFromDownloads(downloaded)
                    }
                }
                if (it.isEmpty())
                    showEmptyState(R.layout.view_download_empty_state)

                binding.rvDownloads.layoutManager = LinearLayoutManager(requireContext())
                adapter = DownloadAdapter(it as ArrayList, imageLoadingService, this)
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRemoveClick(download: Download) {
        viewModel.removeFromDownloads(download)
        val file = File(download.path)
        file.delete()
    }

    override fun onEpisodeClick(download: Download) {
//        val episode = Episode(
//            downloaded.duration,
//            downloaded.id,
//            downloaded.image,
//            downloaded.name,
//            downloaded.season_id,
//            downloaded.path
//        )
//        startActivity(Intent(requireContext(), PlayerActivity::class.java).apply {
//            putExtra(EXTRA_KEY_ID, episode)
//        })
    }

    override fun onResume() {
        super.onResume()

        FirebaseAnalytics.getInstance(requireContext())
            .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, "DownloadedFragment")
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.javaClass.simpleName)
            })
    }
}