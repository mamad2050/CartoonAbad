package ir.andromeda.cartoonabad.feature.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.common.EXTRA_KEY_ID
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.databinding.FragmentListBinding
import ir.andromeda.cartoonabad.feature.player.PlayerActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ListFragment : CartoonAbadFragment(), EpisodeEventListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var adapter: SeasonAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: ListViewModel by viewModel { parametersOf(args.animation.id) }

    private val args: ListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        viewModel.seasonsLiveData.observe(viewLifecycleOwner) {

            binding.rvSeasons.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = SeasonAdapter(it, imageLoadingService, requireContext(), this)
            binding.rvSeasons.adapter = adapter

        }
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

    override fun onEpisodeClick(episode: Episode) {
        startActivity(Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, episode)
        })
    }

    override fun onFavoriteClick(episode: Episode) {
        viewModel.addEpisodeToFavorites(episode)
    }
}