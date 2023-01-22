package ir.andromeda.movieado.services.imageloader

import com.facebook.drawee.view.SimpleDraweeView
import ir.andromeda.movieado.view.MovieadoImageView
import java.lang.IllegalStateException

class FrescoImageLoadingService : ImageLoadingService {
    override fun load(imageView: MovieadoImageView, imageUrl: String) {
        if (imageView is SimpleDraweeView)
            imageView.setImageURI(imageUrl)
        else
            throw IllegalStateException("ImageView must be instance of SimpleDraweeView")
    }
}