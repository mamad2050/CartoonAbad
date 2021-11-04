package ir.andromeda.cartoonabad.feature.main

import android.os.Bundle
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.databinding.ActivityMainBinding
import timber.log.Timber
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ir.andromeda.cartoonabad.BuildConfig
import ir.andromeda.cartoonabad.common.*
import ir.andromeda.cartoonabad.data.PurchaseContainer
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.Payment
import org.koin.android.ext.android.inject
import java.lang.Exception
import java.util.*

class MainActivity : CartoonAbadActivity(), DrawerLocker {

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityMainBinding

    private val payment: Payment by inject()
    private lateinit var paymentConnection: Connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkSubscription()
        setSupportActionBar(binding.toolbar)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.rateMenuItem -> {
                    rateApp()
                }
                R.id.shareMenuItem -> {
                    shareApp()
                }
                R.id.contactsFragment -> {
                    navController.navigate(R.id.navigateToContactsFragment)
                }
                R.id.downloadedFragment -> {
                    if (PurchaseContainer.purchaseInfo == null) {
                        navController.navigate(R.id.navigateToPurchaseAlertDialog)
                    } else {
                        navController.navigate(R.id.navigateToDownloadedFragment)
                    }
                }
                R.id.purchaseFragment -> {
                    navController.navigate(R.id.navigateToPurchaseFragment)
                }
                R.id.favoriteFragment -> {
                    navController.navigate(R.id.navigateToFavoriteFragment)
                }
            }
            binding.drawerLayout.close()
            true
        }
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

    private fun setSubscriptionDays(purchaseTime: Long, orderId: String) {
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
        binding.navigationView.findViewById<TextView>(R.id.tvDrawerDays).text =
            getString(R.string.you) + " " + subscriptionDays + " " + getString(R.string.day_have_subscription)
        binding.navigationView.findViewById<TextView>(R.id.tvDrawerDays)
            .setTextColor(ContextCompat.getColor(this, R.color.green))

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

    override fun onSupportNavigateUp(): Boolean {
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun setDrawerLocked(shouldLock: Boolean) {
        if (shouldLock) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

}

interface DrawerLocker {
    fun setDrawerLocked(shouldLock: Boolean)
}