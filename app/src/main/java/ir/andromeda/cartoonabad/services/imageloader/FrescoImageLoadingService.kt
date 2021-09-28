package ir.andromeda.cartoonabad.services.imageloader

import com.facebook.drawee.view.SimpleDraweeView
import ir.andromeda.cartoonabad.view.CartoonAbadImageView
import java.lang.IllegalStateException

class FrescoImageLoadingService : ImageLoadingService {
    override fun load(imageView: CartoonAbadImageView, imageUrl: String) {
        if (imageView is SimpleDraweeView)
            imageView.setImageURI(imageUrl)
        else
            throw IllegalStateException("ImageView must be instance of SimpleDraweeView")
    }
}