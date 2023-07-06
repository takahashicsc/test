package jp.co.shinoken.ui.fragment.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentTermsBinding

@AndroidEntryPoint
class TermsFragment : Fragment() {

    private lateinit var binding: FragmentTermsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTermsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    companion object {
        fun newInstance() = TermsFragment()
    }
}