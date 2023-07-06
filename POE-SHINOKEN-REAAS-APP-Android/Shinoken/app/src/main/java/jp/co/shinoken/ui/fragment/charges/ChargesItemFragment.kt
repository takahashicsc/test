package jp.co.shinoken.ui.fragment.charges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.model.Charge
import jp.co.shinoken.databinding.FragmentChargesItemBinding
import jp.co.shinoken.ui.fragment.charges.item.ChargesItem
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChargesItemFragment : Fragment() {

    private val viewModel: ChargesItemViewModel by viewModels()
    private lateinit var binding: FragmentChargesItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentChargesItemBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.charges.collect {
                if (it.isEmpty()) return@collect

                val adapter = GroupAdapter<GroupieViewHolder>()

                it.forEach { charge ->
                    adapter.add(ChargesItem(charge) { chargeDate ->
                        findNavController().navigate(
                            ChargesFragmentDirections.actionChargesFragmentToChargeDetailFragment(
                                chargeDate
                            )
                        )
                    })
                }

                binding.recycler.apply {
                    isVisible = it.isNotEmpty()
                    this.adapter = adapter
                    this.layoutManager =
                        LinearLayoutManager(requireContext())
                }
                binding.emptyView.isVisible = it.isEmpty()
            }
        }
    }

    companion object {
        private const val ChargesTypeArgs = "chargesType"
        fun newInstance(charges: List<Charge>) = ChargesItemFragment().apply {
            arguments = bundleOf(ChargesTypeArgs to charges)
        }
    }
}