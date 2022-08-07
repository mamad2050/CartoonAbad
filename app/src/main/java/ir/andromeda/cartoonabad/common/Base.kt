package ir.andromeda.cartoonabad.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ir.andromeda.cartoonabad.R
import java.lang.IllegalStateException


abstract class CartoonAbadFragment() : Fragment(), CartoonAbadView {
    override val rootView: CoordinatorLayout
        get() = view as CoordinatorLayout
    override val viewContext: Context?
        get() = context
}

abstract class CartoonAbadActivity : AppCompatActivity(), CartoonAbadView {
    override val rootView: CoordinatorLayout?
        get() {
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            if (viewGroup !is CoordinatorLayout) {
                viewGroup.children.forEach {
                    if (it is CoordinatorLayout)
                        return it
                }
                throw IllegalStateException("RootView must be instance of CoordinatorLayout")
            } else
                return viewGroup
        }
    override val viewContext: Context?
        get() = this

}

interface CartoonAbadView {

    val rootView: CoordinatorLayout?
    val viewContext: Context?

    fun setProgressIndicator(mustShow: Boolean) {

        rootView?.let {
            viewContext?.let { context ->
                var loadingView = it.findViewById<View>(R.id.loadingView)
                if (loadingView == null && mustShow) {
                    loadingView =
                        LayoutInflater.from(context).inflate(R.layout.view_loading, it, false)
                    it.addView(loadingView)
                }
                loadingView?.visibility = if (mustShow) View.VISIBLE else View.GONE
            }
        }
    }


    fun showEmptyState(layoutResId: Int): View? {

        rootView?.let {
            viewContext?.let { context ->
                var emptyState = it.findViewById<View>(R.id.emptyStateRootView)
                if (emptyState == null) {
                    emptyState = LayoutInflater.from(context).inflate(layoutResId, it, false)
                    it.addView(emptyState)
                }
                emptyState.visibility = View.VISIBLE
                return emptyState
            }
        }
        return null
    }

    fun showConnectionLost(mustShow: Boolean): View? {

        rootView?.let {
            viewContext?.let { context ->
                var connectionLost = it.findViewById<View>(R.id.connectionLostRootView)
                if (connectionLost == null) {
                    connectionLost = LayoutInflater.from(context)
                        .inflate(R.layout.view_connection_lost, it, false)
                    it.addView(connectionLost)
                }
                connectionLost.visibility = if (mustShow) View.VISIBLE else View.GONE
                return connectionLost
            }
        }
        return null
    }

}

abstract class CartoonAbadViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    val progressBarLiveData = MutableLiveData<Boolean>()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}

interface OnItemEventListener<in V> {
    fun clickOnItem(item: V)
}