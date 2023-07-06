package jp.co.shinoken.ui.fragment.lifeline_contact

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentLifelineContactsBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.ui.fragment.lifeline_contact.item.LifelineItem
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LifelineContactsFragment : Fragment() {

    private val viewModel: LifelineContactsViewModel by viewModels()
    private lateinit var binding: FragmentLifelineContactsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLifelineContactsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()

        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(context)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                if (it) {
                    binding.lifelinesProgressBar.show()
                    binding.lifelineEmptyView.isVisible = false
                } else {
                    binding.lifelinesProgressBar.hide()
                }

                binding.lifelinesProgressBar.isGone = it.not()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.lifelines.collect {
                binding.lifelineEmptyView.apply {
                    setEmptyText(getString(R.string.empty_text_format).format(getString(R.string.life_line_contacts_title)))
                    isVisible = it.isEmpty()
                }
                binding.lifelineView.isGone = it.isEmpty()

                if (it.isEmpty()) return@collect
                adapter.update(
                    it.map { lifeline ->
                        LifelineItem(lifeline) { phoneNumber ->
                            val telSchemeNumber = "tel:${phoneNumber}".toUri()
                            startActivity(Intent(Intent.ACTION_DIAL, telSchemeNumber))
                        }
                    }
                )

                binding.lifelineEmptyElectric.isVisible =
                    it.firstOrNull { lifeline -> lifeline.kind == "電気" } != null
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
        fun newInstance() = LifelineContactsFragment()
    }
}