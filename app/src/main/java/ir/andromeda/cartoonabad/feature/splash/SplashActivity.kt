package ir.andromeda.cartoonabad.feature.splash

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import ir.andromeda.cartoonabad.common.CartoonAbadActivity
import ir.andromeda.cartoonabad.databinding.ActivitySplashBinding
import ir.andromeda.cartoonabad.feature.main.MainActivity

class SplashActivity : CartoonAbadActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        checkCondition()

        binding.btnExit.setOnClickListener {
            onBackPressed()
        }

    }

    private fun checkNetworkConnection(): Boolean {

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = cm.activeNetwork ?: return false
            val actNw = cm.getNetworkCapabilities(network) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }

        } else
            return false
    }

    private fun goToHomeActivity() {

        binding.pbSplash.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun checkCondition() {

        if (checkNetworkConnection()) {
            goToHomeActivity()
        } else {
            noNetwork()
        }
    }

    private fun noNetwork() {
        binding.isOnlineLayout.visibility = View.GONE
        binding.pbSplash.visibility = View.GONE
        binding.isOfflineLayout.visibility = View.VISIBLE
    }

}