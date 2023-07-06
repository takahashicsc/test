package jp.co.shinoken.ui.fragment.cohabitant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentCohabitantsBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.ui.fragment.cohabitant.item.CohabitantsItem
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CohabitantsFragment : Fragment() {

    private val viewModel: CohabitantsViewModel by viewModels()
    private lateinit var binding: FragmentCohabitantsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCohabitantsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetch()
        binding.addButton.setOnClickListener {
            findNavController().navigate(CohabitantsFragmentDirections.actionCohabitantsFragmentToCohabitantFragment())
        }

        val adapter = GroupAdapter<GroupieViewHolder>()

        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(context)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.cohabitants.collect {
                if (it.isEmpty()) return@collect
                adapter.update(it.filter { it.cohabitants.isNullOrEmpty().not() }.map { item ->
                    CohabitantsItem(items = item) { cohabitant, resideType ->
                        findNavController().navigate(
                            CohabitantsFragmentDirections.actionCohabitantsFragmentToCohabitantDetailFragment(
                                cohabitant,
                                resideType
                            )
                        )
                    }
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                if (it) {
                    binding.cohabitantsProgressBar.show()
                } else {
                    binding.cohabitantsProgressBar.hide()
                }

                binding.cohabitantsProgressBar.isGone = it.not()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it) { viewModel.fetch() }
            }
        }
    }

    companion object {
        fun newInstance() = CohabitantsFragment()
    }
}