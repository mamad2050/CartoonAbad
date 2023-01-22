package ir.andromeda.movieado.services.imageloader

import ir.andromeda.movieado.view.MovieadoImageView

interface ImageLoadingService {
    fun load(imageView: MovieadoImageView, imageUrl: String)
}