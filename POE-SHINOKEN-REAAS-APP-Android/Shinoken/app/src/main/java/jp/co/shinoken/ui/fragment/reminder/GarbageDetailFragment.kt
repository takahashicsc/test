package jp.co.shinoken.ui.fragment.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentGarbageDetailBinding

@AndroidEntryPoint
class GarbageDetailFragment : Fragment() {

    private val viewModel: GarbageDetailViewModel by viewModels()
    private lateinit var binding: FragmentGarbageDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentGarbageDetailBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.garbageSelectType.setOnClickListener {
            findNavController().navigate(GarbageDetailFragmentDirections.actionGarbageDetailFragmentToCollectionDateTypeFormFragment())
        }
    }

    companion object {
        fun newInstance() = GarbageDetailFragment()
    }
}