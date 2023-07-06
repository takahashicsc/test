package jp.co.shinoken.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.BuildConfig
import jp.co.shinoken.R
import jp.co.shinoken.extension.showAlertDialog
import jp.co.shinoken.repository.StoreRepository
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    @Inject
    lateinit var storeRepository: StoreRepository

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initUUID()
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
        } catch (e: Exception) {
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenCreated {
            viewModel.launcherNavigation.collect {
                it ?: return@collect
                when (it) {
                    LauncherViewModel.LauncherNavigation.WalkThrough -> {
                        showMainActivity(isLogin = false)
                    }

                    LauncherViewModel.LauncherNavigation.Login -> {
                        showMainActivity(isLogin = true)
                    }

                    LauncherViewModel.LauncherNavigation.Update -> {
                        showForcedUpdateDialog()
                    }
                }
            }
        }
    }

    private fun showMainActivity(isLogin: Boolean) {
        startActivity(MainActivity.createIntent(this@LauncherActivity, isLogin))
    }

    private fun showForcedUpdateDialog() {
        showAlertDialog(
            title = getString(R.string.launcher_screen_error_title),
            message = getString(R.string.launcher_screen_error_description),
            positiveListener = {
                val uriBuilder = "https://play.google.com/store/apps/details".toUri()
                    .buildUpon()
                    .appendQueryParameter("id", "com.shinoken.conceirge.user")
                    /*.appendQueryParameter("id", BuildConfig.APPLICATION_ID)*//*
                    .appendQueryParameter("launch", "true")*/

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = uriBuilder.build()
                    setPackage("com.android.vending")
                }
                startActivity(intent)
            },
            negativeText = getString(R.string.cancel),
            negativeListener = {
                showForcedUpdateDialog()
            },
            cancelable = false
        )
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, LauncherActivity::class.java)
    }
}