package ir.andromeda.movieado.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ir.andromeda.movieado.R
import ir.andromeda.movieado.common.*
import ir.andromeda.movieado.data.banner.Banner
import ir.andromeda.movieado.feature.detail.DetailMovieActivity
import ir.andromeda.movieado.feature.detail.DetailSeriesActivity
import ir.andromeda.movieado.services.imageloader.ImageLoadingService
import ir.andromeda.movieado.view.MovieadoImageView
import org.koin.android.ext.android.inject
import java.lang.IllegalStateException

class BannerFragment : Fragment() {

    val imageLoadingService: ImageLoadingService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_banner, container, false)

        val title: TextView = view.findViewById(R.id.tv_banner)
        val image: MovieadoImageView = view.findViewById(R.id.iv_banner)

        val banner =
            requireArguments().getParcelable<Banner>(EXTRA_KEY_DATA) ?: throw IllegalStateException(
                "Banner can not be null"
            )

        title.text = banner.title
        imageLoadingService.load(image, banner.image)

        view.setOnClickListener {
            if (banner.type == TYPE_MOVIE && banner.type == TYPE_ANIMATION_MOVIE) {
                startActivity(Intent(requireActivity(), DetailMovieActivity::class.java).apply {
                    putExtra(EXTRA_KEY_ID, banner.movieId)
                })
            } else if (banner.type == TYPE_SERIES && banner.type == TYPE_ANIMATION_SERIES) {
                startActivity(Intent(requireActivity(), DetailSeriesActivity::class.java).apply {
                    putExtra(EXTRA_KEY_ID, banner.movieId)
                })
            }
        }

        return view
    }

    companion object {
        fun newInstance(banner: Banner): BannerFragment {
            return BannerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_KEY_DATA, banner)
                }
            }

        }
    }
}