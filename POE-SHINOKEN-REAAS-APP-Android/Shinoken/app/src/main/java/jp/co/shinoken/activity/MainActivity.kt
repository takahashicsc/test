package jp.co.shinoken.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.extension.getCurrentFragment
import jp.co.shinoken.extension.hideKeyboard
import jp.co.shinoken.extension.saveCashImage
import jp.co.shinoken.extension.showAlertDialog
import jp.co.shinoken.model.api.Link
import jp.co.shinoken.ui.PageConfiguration
import jp.co.shinoken.ui.fragment.account.AccountFragment
import jp.co.shinoken.ui.fragment.check_form.CheckFormFragment
import jp.co.shinoken.ui.fragment.faq.FaqDetailFragment
import jp.co.shinoken.ui.fragment.home.HomeFragment
import jp.co.shinoken.ui.fragment.login.LoginFragment
import jp.co.shinoken.ui.fragment.notification.detail.NotificationDetailFragment
import jp.co.shinoken.ui.fragment.search.SearchFragment
import jp.co.shinoken.ui.fragment.sign_up.SignUpCodeFormFragment
import jp.co.shinoken.ui.fragment.walk_through.WalkThroughFragment
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AccountFragment.IAccountFragment,
    FaqDetailFragment.IFaqDetailFragment,
    SearchFragment.ISearchFragment,
    NotificationDetailFragment.INotificationDetailFragment,
    SignUpCodeFormFragment.ISignUpCodeFormFragment,
    CheckFormFragment.ICheckFormFragment {
    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_main)
    }

    private val viewModel: MainViewModel by viewModels()

    private val requestExternalStorage = 1
    private val permissionStorage =
            if (Build.VERSION.SDK_INT > 32) {
                arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES
            )
            } else {
                arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
        } catch (e: Exception) {
        }
        setContentView(R.layout.activity_main)

        val appBarConfiguration =
            AppBarConfiguration(
                topLevelDestinationIds = setOf(
                    R.id.homeFragment,
                    R.id.walkThroughFragment,
                    R.id.loginFragment
                ),
            )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            onDestinationChange(destination)
        }

        val permission =
                if (Build.VERSION.SDK_INT > 32) {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                } else {
                    ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_MEDIA_IMAGES
                    )
                }

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                permissionStorage,
                requestExternalStorage
            )
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        if (savedInstanceState == null) {
            val isLogin: Boolean = intent.getBooleanExtra(ArgsIsLogin, false)
            navigate(
                if (isLogin) {
                    R.id.homeFragment
                } else {
                    R.id.walkThroughFragment
                }
            )
        }
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
        hideKeyboard()
        val currentFragment = getCurrentFragment(R.id.nav_host_main)
        if (currentFragment is LoginFragment ||
            currentFragment is WalkThroughFragment ||
            currentFragment is HomeFragment
        ) {
            // 戻る禁止画面
            return
        }
        super.onBackPressed()
    }

    private fun onDestinationChange(destination: NavDestination) {
        val config = PageConfiguration.getConfiguration(destination.id)
        if (config.hideToolbar) {
            supportActionBar?.hide()
        } else {
            supportActionBar?.show()
        }
    }

    private val cropImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val uri: Uri? = if (result.data?.extras?.get(ResultUri) != null) {
                        result.data?.extras?.get(ResultUri)!! as Uri
                    } else {
                        val bitmap = result.data?.extras?.get(ResultBitmap) as Bitmap?
                            ?: return@launch
                        saveCashImage(bitmap)
                    }
                    uri ?: return@launch
                    when (val currentFragment = getCurrentFragment(R.id.nav_host_main)) {
                        is AccountFragment -> {
                            currentFragment.setProfileImage(uri)
                        }
                        is CheckFormFragment -> {
                            currentFragment.setImage(uri)
                        }
                    }
                }
            }
        }

    private val startSelectImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.data == null) {
                    lifecycleScope.launch {
                        val bitmap = result.data?.extras?.get("data") as Bitmap?
                            ?: return@launch
                        val uri = saveCashImage(bitmap) ?: return@launch
                        when (val currentFragment = getCurrentFragment(R.id.nav_host_main)) {
                            is AccountFragment -> {
                                currentFragment.setProfileImage(uri)
                            }
                            is CheckFormFragment -> {
                                currentFragment.setImage(uri)
                            }
                        }
                    }
                } else {
                    val uri = result.data?.data!!
                    val intent = CropActivity.createIntent(this, uri)
                    cropImageForResult.launch(intent)
                }
            }
        }
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if ((result.filter { it.value == true }).size == result.size) {
                //カメラの起動Intentの用意
                val intentCamera = Intent()
                    .setAction(MediaStore.ACTION_IMAGE_CAPTURE)

                // ギャラリー用のIntent作成
                val intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intentGallery.addCategory(Intent.CATEGORY_OPENABLE)
                intentGallery.type = "image/*"
                val intent = Intent.createChooser(intentCamera, "画像の選択")
                intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intentGallery))
                intent.apply {
                    addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                startSelectImageForResult.launch(intent)
            } else {
                showAlertDialog(
                    message = getString(R.string.permission_storage_error_message),
                )
            }
        }

    private val startSignUpErrorActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                navigate(R.id.loginFragment)
            }
        }

    override fun selectImage() {
        requestPermission.launch(
                if (Build.VERSION.SDK_INT > 32) {
                    arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES
                    )
                } else {
                    arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
        )
    }

    override fun selectImageUri() {
        requestPermission.launch(
                if (Build.VERSION.SDK_INT > 32) {
                    arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES
                    )
                } else {
                    arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
        )
    }

    override fun navigate(fragmentId: Int) {
        Navigation.findNavController(this, R.id.nav_host_main)
            .navigate(fragmentId)
    }

    fun signOut() {
        viewModel.signOut()
        Navigation.findNavController(this, R.id.nav_host_main).navigate(R.id.loginFragment)
    }

    companion object {
        const val ResultUri = "ResultUri"
        const val ResultBitmap = "ResultBitmap"
        const val ArgsIsLogin = "ArgsIsLogin"

        fun createIntent(
            context: Context,
            isLogin: Boolean
        ): Intent = Intent(context, MainActivity::class.java).apply {
            putExtra(ArgsIsLogin, isLogin)
        }
    }

    override fun navigateScreen(faqLink: Link) {
        navController.navigate(Uri.parse(faqLink.url))
    }

    override fun showSignUpErrorActivity() {
        startSignUpErrorActivityForResult.launch(Intent(this, SignUpErrorActivity::class.java))
    }
}