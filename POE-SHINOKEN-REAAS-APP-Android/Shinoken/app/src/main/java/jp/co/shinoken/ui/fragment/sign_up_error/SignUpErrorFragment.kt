package jp.co.shinoken.ui.fragment.sign_up_error

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentSignUpErrorBinding

class SignUpErrorFragment : Fragment() {

    private lateinit var binding: FragmentSignUpErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSignUpErrorBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpErrorPhoneNumber.setOnClickListener {
            val telPhone = getString(R.string.sign_up_error_contact_phone_number)
            val telSchemeNumber = "tel:${telPhone}".toUri()
            startActivity(Intent(Intent.ACTION_DIAL, telSchemeNumber))
        }

        binding.signUpErrorEmail.setOnClickListener {
            findNavController().navigate(SignUpErrorFragmentDirections.actionSignUpErrorFragmentToSignInSupportMailFormFragment2())
        }

        binding.signUpErrorClose.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    companion object {
        fun newInstance() = SignUpErrorFragment().apply {}
    }
}