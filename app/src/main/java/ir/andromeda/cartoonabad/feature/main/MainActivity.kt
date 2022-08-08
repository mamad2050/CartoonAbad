package ir.andromeda.cartoonabad.feature.main

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.databinding.ActivityMainBinding
import timber.log.Timber
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import ir.andromeda.cartoonabad.BuildConfig
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.PurchaseContainer
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.Payment
import org.koin.android.ext.android.inject
import java.lang.Exception
import java.util.*

class MainActivity : CartoonAbadActivity() {

    private lateinit var binding: ActivityMainBinding
    private val payment: Payment by inject()
    private lateinit var paymentConnection: Connection
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkSubscription()
//        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationMain)

        val navGraphIds = listOf(
            R.navigation.home,
            R.navigation.genre,
            R.navigation.download,
            R.navigation.purchase
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.fragment_container,
            intent = intent
        )
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun checkSubscription() {
        paymentConnection = payment.connect {
            connectionSucceed {
                payment.getSubscribedProducts {
                    querySucceed { purchasedItems ->
                        if (purchasedItems.isNotEmpty()) {
                            PurchaseContainer.setPurchaseInfo(purchasedItems[0])
                            setSubscriptionDays(
                                purchasedItems[0].purchaseTime,
                                purchasedItems[0].productId
                            )
                        }
                    }
                    queryFailed {
                        snackBar(getString(R.string.connection_to_bazaar_failed))
                    }
                }
            }
            connectionFailed {
                snackBar(getString(R.string.install_bazaar_app))
            }
            disconnected {
            }
        }
    }

    fun setSubscriptionDays(purchaseTime: Long, orderId: String) {
        val day: Long = when (orderId) {
            YEARLY_PRODUCT_ID -> 365L
            THREE_MONTH_PRODUCT_ID -> 90L
            MONTHLY_PRODUCT_ID -> 30L
            else -> 0L
        }
        val endTime: Long = purchaseTime + (86400000L * (day + 1))
        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()
        endCalendar.timeInMillis = endTime
        val subscriptionDays =
            ((endCalendar.timeInMillis - startCalendar.timeInMillis) / 86400000L).toString()
    }

    private fun snackBar(message: String) {
        Snackbar.make(
            binding.contentRootView, message, Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun rateApp() {
        //cafebazaar rate
        try {
            val intent = Intent(Intent.ACTION_EDIT)
            intent.data = Uri.parse("bazaar://details?id=${BuildConfig.APPLICATION_ID}")
            intent.setPackage("com.farsitel.bazaar")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.install_bazaar_app), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            var shareMessage =
                "سلااااام، توی برنامه ی کارتون آباد میتونی کارتون های معروف و قشنگی رو بصورت رایگان ببینی.\n\nاز لینک زیر دانلودش کن."
            //cafebazaar link
            shareMessage =
                "${shareMessage}\n\nhttps://cafebazaar.ir/app/${BuildConfig.APPLICATION_ID}"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_app)))
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        paymentConnection.disconnect()
    }


}
