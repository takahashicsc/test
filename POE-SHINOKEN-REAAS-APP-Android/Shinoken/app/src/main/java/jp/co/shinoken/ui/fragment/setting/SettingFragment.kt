package jp.co.shinoken.ui.fragment.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.activity.MainActivity
import jp.co.shinoken.databinding.FragmentSettingBinding
import jp.co.shinoken.extension.showAlertDialog
import jp.co.shinoken.extension.showChromeCustomTabs
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels()

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSettingBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.licenseButton.setOnClickListener {
            startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java))
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.license_title))
        }
        binding.passwordChangeButton.setOnClickListener {
            findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToPasswordChangeFragment())
        }

        lifecycleScope.launchWhenCreated {
            viewModel.termsUrl.collect { termsUri ->
                binding.termsButton.run {
                    isVisible = termsUri != null
                    termsUri ?: return@collect
                    setOnClickListener {
                        showChromeCustomTabs(termsUri.toUri())
                    }
                }
            }
        }

        binding.appVersionText.text = requireContext().packageManager.getPackageInfo(
            requireContext().packageName,
            0
        ).versionName

        binding.logoutButton.setOnClickListener {
            showAlertDialog(
                message = getString(R.string.logout_confirm_message),
                positiveListener = {
                    val activity = requireActivity()
                    if (activity is MainActivity) {
                        activity.signOut()
                    }
                },
                negativeText = getString(R.string.cancel)
            )

        }
    }

    companion object {
        fun newInstance() = SettingFragment()
    }
}