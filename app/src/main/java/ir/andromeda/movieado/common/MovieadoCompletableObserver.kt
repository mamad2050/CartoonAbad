package ir.andromeda.movieado.common

import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

abstract class MovieadoCompletableObserver(private val compositeDisposable: CompositeDisposable) :
    CompletableObserver {
    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }

    override fun onError(e: Throwable) {
        EventBus.getDefault().post(MovieadoExceptionMapper.map(e))
        Timber.e(e)
    }
}