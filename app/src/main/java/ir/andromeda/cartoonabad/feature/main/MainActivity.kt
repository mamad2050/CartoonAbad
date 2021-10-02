package ir.andromeda.cartoonabad.feature.main

import android.os.Bundle
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadActivity
import ir.andromeda.cartoonabad.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : CartoonAbadActivity(), DrawerLocker {

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
                    Toast.makeText(this, "rateMenuItem", Toast.LENGTH_SHORT).show()
                }
                R.id.shareMenuItem -> {
                    Toast.makeText(this, "shareMenuItem", Toast.LENGTH_SHORT).show()
                }
                R.id.contactsMenuItem -> {
                    Toast.makeText(this, "contactsMenuItem", Toast.LENGTH_SHORT).show()
                }
                R.id.downloadedFragment -> {
                    navController.navigate(R.id.navigateToDownloadedFragment)
                }
                R.id.purchaseFragment -> {
//                    navController.navigate(R.id.navigateToPurchaseFragment)
                }
                R.id.favoriteFragment -> {
                    navController.navigate(R.id.navigateToFavoriteFragment)
                }
            }
            drawerLayout.close()
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun setDrawerLocked(shouldLock: Boolean) {
        if (shouldLock) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

}

interface DrawerLocker {
    fun setDrawerLocked(shouldLock: Boolean)
}