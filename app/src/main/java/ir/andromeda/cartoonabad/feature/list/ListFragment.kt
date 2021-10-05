package ir.andromeda.cartoonabad.feature.list

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.EXTERNAL_STORAGE_PERMISSION_KEY
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.databinding.FragmentListBinding
import ir.andromeda.cartoonabad.feature.dwnloaded.DownloadedFragment
import ir.andromeda.cartoonabad.feature.main.DrawerLocker
import ir.andromeda.cartoonabad.feature.player.PlayerActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.security.Permission
import java.util.jar.Manifest

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

        (activity as DrawerLocker).setDrawerLocked(true)

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
        (activity as DrawerLocker).setDrawerLocked(false)
    }

    override fun onEpisodeClick(episode: Episode) {
        startActivity(Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, episode)
        })
    }

    override fun onFavoriteClick(episode: Episode) {
        viewModel.addEpisodeToFavorites(episode)
    }

    override fun onDownloadClick(episode: Episode) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (requireActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                startDownloading(episode)
            } else {
                requireActivity().requestPermissions(
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    EXTERNAL_STORAGE_PERMISSION_KEY
                )
            }

        } else {
            startDownloading(episode)
        }

//        view?.let { Navigation.findNavController(it).navigate(R.id.action_listFragment_to_downloadedFragment)}
    }

    private fun startDownloading(episode: Episode) {

        val request = DownloadManager.Request(Uri.parse(episode.url))
        request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(episode.name)
        request.setDescription(episode.duration)
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
           "CartoonAbad/" + episode.url.substringAfterLast("/")
        )

        val manager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_KEY && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {

        }
        else
            Snackbar.make(binding.root,"You should accept permission",Snackbar.LENGTH_SHORT).show()

    }

}
