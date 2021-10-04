package ir.andromeda.cartoonabad.feature.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.common.OnItemEventListener
import ir.andromeda.cartoonabad.data.EmptyState
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.databinding.FragmentFavoriteBinding
import ir.andromeda.cartoonabad.feature.main.DrawerLocker
import ir.andromeda.cartoonabad.feature.player.PlayerActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import kotlinx.android.synthetic.main.fragment_favorite.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class FavoriteFragment : CartoonAbadFragment(), FavoriteAdapter.EpisodeEventListener {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private var adapter: FavoriteAdapter? = null
    private val imageLoadingService: ImageLoadingService by inject()
    private val viewModel: FavoriteViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as DrawerLocker).setDrawerLocked(true)

        viewModel.episodesLiveData.observe(viewLifecycleOwner) {

            if (it.isNotEmpty()) {
                binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
                adapter = FavoriteAdapter(it as ArrayList, imageLoadingService, this)
                binding.rvFavorites.adapter = adapter
                Timber.i(it.toString())
            }

        }

        viewModel.emptyStateLiveData.observe(viewLifecycleOwner){
            if (it.mustShow) {
                showEmptyState(R.layout.view_default_empty_state)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        (activity as DrawerLocker).setDrawerLocked(false)
    }

    override fun onRemoveClick(episode: Episode) {
        viewModel.removeFromFavorite(episode)
    }

    override fun onEpisodeClick(episode: Episode) {
        startActivity(Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, episode)
        })
    }

}