package ir.andromeda.cartoonabad.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CARTOON
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.common.EXTRA_KEY_ID
import ir.andromeda.cartoonabad.common.SERIES
import ir.andromeda.cartoonabad.data.banner.Banner
import ir.andromeda.cartoonabad.feature.detail.DetailCartoonActivity
import ir.andromeda.cartoonabad.feature.detail.DetailSeriesActivity
import ir.andromeda.cartoonabad.services.imageloader.ImageLoadingService
import ir.andromeda.cartoonabad.view.CartoonAbadImageView
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
        val image: CartoonAbadImageView = view.findViewById(R.id.iv_banner)

        val banner =
            requireArguments().getParcelable<Banner>(EXTRA_KEY_DATA) ?: throw IllegalStateException(
                "Banner can not be null"
            )

        title.text = banner.title
        imageLoadingService.load(image, banner.image)

        view.setOnClickListener {
            if (banner.type == CARTOON) {
                startActivity(Intent(requireActivity(), DetailCartoonActivity::class.java).apply {
                    putExtra(EXTRA_KEY_ID, banner.content_id)
                })
            }
            if (banner.type == SERIES) {
                startActivity(Intent(requireActivity(), DetailSeriesActivity::class.java).apply {
                    putExtra(EXTRA_KEY_ID, banner.content_id)
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