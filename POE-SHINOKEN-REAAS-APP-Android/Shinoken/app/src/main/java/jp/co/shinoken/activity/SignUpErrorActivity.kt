package jp.co.shinoken.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.extension.getCurrentFragment
import jp.co.shinoken.ui.fragment.home.HomeFragment
import jp.co.shinoken.ui.fragment.login.LoginFragment
import jp.co.shinoken.ui.fragment.sign_in_support.SignInSupportMailFormFragment
import jp.co.shinoken.ui.fragment.walk_through.WalkThroughFragment

@AndroidEntryPoint
class SignUpErrorActivity : AppCompatActivity() {
    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_sign_up_error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_error)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val appBarConfiguration =
            AppBarConfiguration(topLevelDestinationIds = setOf())
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment(R.id.nav_host_sign_up_error)
        if (currentFragment is SignInSupportMailFormFragment
        ) {
            super.onBackPressed()
            return
        }
        setResult(RESULT_OK, Intent())
        finish()
    }
}