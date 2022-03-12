package ir.andromeda.cartoonabad.feature.detail

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import ir.andromeda.cartoonabad.common.CartoonAbadViewModel
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.data.cartoon.Cartoon
import ir.andromeda.cartoonabad.data.download.DownloadedRepository

class DetailCartoonViewModel(
    bundle: Bundle,
    private val downloadRepository: DownloadedRepository
) :
    CartoonAbadViewModel() {

    val cartoonLiveData = MutableLiveData<Cartoon>()

    init {
        progressBarLiveData.value = true
        cartoonLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)
        progressBarLiveData.value = false
    }

//    fun addEpisodeToFavorites(episode: Episode) {
//        if (episode.isFavorite)
//            episodeRepository.deleteFromFavorite(episode)
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
//                    override fun onComplete() {
//                        episode.isFavorite = false
//                    }
//                })
//        else
//            episodeRepository.addToFavorite(episode)
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
//                    override fun onComplete() {
//                        episode.isFavorite = true
//                    }
//                })
//    }
//
//    fun addEpisodeToDownloads(episode: Downloaded) {
//        if (episode.isDownload)
//            downloadRepository.deleteFromDownloads(episode)
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
//                    override fun onComplete() {
//                        episode.isDownload = false
//                    }
//                })
//        else
//            downloadRepository.addToDownloads(episode)
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : CartoonAbadCompletableObserver(compositeDisposable) {
//                    override fun onComplete() {
//                        episode.isDownload = true
//                    }
//                })
//    }
}

