package jp.co.shinoken.ui.fragment.charges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentChargeDetailBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.ui.fragment.charges.item.ChargeDetailItem
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChargeDetailFragment : Fragment() {

    private val viewModel: ChargeDetailViewModel by viewModels()
    private lateinit var binding: FragmentChargeDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentChargeDetailBinding.inflate(inflater, container, false).also {
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
                    binding.chargeDetailProgressBar.show()
                } else {
                    binding.chargeDetailProgressBar.hide()
                }

                binding.chargeDetailView.isGone = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.chargeDetail.collect {
                it ?: return@collect

                binding.chargeUpdatedAt.text =
                    getString(R.string.charge_updated_at_format).format(it.getUpdatedAtFormatString())

                it.getSectionAtFormatString()?.let { sectionDate ->
                    binding.sectionDate.text =
                        getString(R.string.charge_detail_section_date_format).format(
                            sectionDate.first,
                            sectionDate.second
                        )
                }
                val list = it.items
                list.forEach { charge ->
                    adapter.add(ChargeDetailItem(charge) {

                    })
                }

                binding.totalFee.text = getString(R.string.bill_detail_fee_format).format(it.total)
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
        fun newInstance() = ChargeDetailFragment()
    }
}