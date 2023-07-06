package jp.co.shinoken.ui.fragment.charges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentChargesBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.model.Charge
import jp.co.shinoken.model.ChargesType
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChargesFragment : Fragment() {

    private lateinit var binding: FragmentChargesBinding
    private val viewModel: ChargesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentChargesBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect {
                if (it) {
                    binding.chargesProgressBar.show()
                } else {
                    binding.chargesProgressBar.hide()
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.charges.collect {
                if (it.items.isEmpty()) {
                    binding.emptyView.run {
                        setEmptyText(getString(R.string.empty_text_format).format(getString(R.string.charges_title)))
                        isVisible = true
                        binding.chargeView.isGone = true
                    }
                    return@collect
                }
                binding.emptyView.isGone = true
                binding.chargeView.isVisible = true

                val chargesValues = it.items

                binding.chargesUpdatedAt.text =
                    getString(R.string.charge_updated_at_format).format(it.getLastUpdatedAtFormatString())

                binding.pager.adapter = ChargesPagerAdapter(requireActivity(), chargesValues)

                if (chargesValues.size < 2) {
                    binding.tabLayout.isVisible = false
                    return@collect
                }
                TabLayoutMediator(
                    binding.tabLayout,
                    binding.pager
                ) { tab: TabLayout.Tab, position: Int ->
                    tab.apply {
                        setCustomView(R.layout.item_tab_title)
                        this.customView?.findViewById<TextView>(R.id.tab_text)?.text =
                            getString(
                                when (chargesValues[position][0].chargesType) {
                                    ChargesType.Electricity -> R.string.electricity
                                    ChargesType.Gas -> R.string.gas
                                    else -> R.string.house_rent
                                }
                            )
                        this.customView?.findViewById<ImageView>(R.id.tab_badge)?.isVisible = false
                    }
                }.attach()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.latestCharge.collect { bill ->
                bill ?: return@collect
                binding.gasFee.text =
                    getString(R.string.fee_format).format(bill.items.filter { it.chargesType == ChargesType.Gas }
                        .map { it.charge }.sum())

                binding.gasFeeLayout.isVisible =
                    bill.items.firstOrNull { it.chargesType == ChargesType.Gas } != null

                binding.electricFee.text =
                    getString(R.string.fee_format).format(bill.items.filter { it.chargesType == ChargesType.Electricity }
                        .map { it.charge }.sum())

                binding.electricFeeLayout.isVisible =
                    bill.items.firstOrNull { it.chargesType == ChargesType.Electricity } != null

                binding.houseRentFee.text =
                    getString(R.string.fee_format).format(bill.items.filter { it.chargesType != ChargesType.Electricity && it.chargesType != ChargesType.Gas }
                        .map { it.charge }.sum())

                binding.houseRentFeeLayout.isVisible =
                    bill.items.firstOrNull { it.chargesType != ChargesType.Electricity && it.chargesType != ChargesType.Gas } != null

                binding.latestMonth.text =
                    getString(R.string.charges_latest_date_format).format(
                        bill.getSectionAtFormatString()?.first,
                        bill.getSectionAtFormatString()?.second
                    )
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
        fun newInstance() = ChargesFragment()
    }


    private inner class ChargesPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val charges: List<List<Charge>>
    ) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = charges.size

        override fun createFragment(position: Int): Fragment {
            return ChargesItemFragment.newInstance(charges = charges[position])
        }
    }
}