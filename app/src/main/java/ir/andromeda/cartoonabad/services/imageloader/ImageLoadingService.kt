package ir.andromeda.cartoonabad.services.imageloader

import ir.andromeda.cartoonabad.view.CartoonAbadImageView

interface ImageLoadingService {
    fun load(imageView: CartoonAbadImageView, imageUrl: String)
}