package ir.andromeda.cartoonabad.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.data.banner.Banner
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
        val imageView =
            inflater.inflate(R.layout.fragment_banner, container, false) as CartoonAbadImageView

        val banner =
            requireArguments().getParcelable<Banner>(EXTRA_KEY_DATA) ?: throw IllegalStateException(
                "Banner can not be null"
            )

        imageLoadingService.load(imageView,banner.image)
        return imageView
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