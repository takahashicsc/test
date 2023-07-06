package jp.co.shinoken.ui.fragment.sign_in_support

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentSignInSupportBinding

@AndroidEntryPoint
class SignInSupportFragment : Fragment() {
    private lateinit var binding: FragmentSignInSupportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSignInSupportBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.supportMailButton.setOnClickListener {
            findNavController().navigate(SignInSupportFragmentDirections.actionSignInSupportFragmentToSignInSupportMailFormFragment())
        }

        binding.signInSupportPhoneNumber.setOnClickListener {
            val telPhone = getString(R.string.sign_up_error_contact_phone_number)
            val telSchemeNumber = "tel:${telPhone}".toUri()
            startActivity(Intent(Intent.ACTION_DIAL, telSchemeNumber))
        }
    }

    companion object {
        fun newInstance() = SignInSupportFragment()
    }
}