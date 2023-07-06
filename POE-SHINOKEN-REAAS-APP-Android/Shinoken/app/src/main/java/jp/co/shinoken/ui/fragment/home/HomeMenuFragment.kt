package jp.co.shinoken.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentHomeMenuBinding
import jp.co.shinoken.model.HomeMenu
import jp.co.shinoken.model.api.Home
import jp.co.shinoken.model.api.Menu
import jp.co.shinoken.ui.fragment.home.item.HomeMenuItem
import jp.co.shinoken.ui.fragment.home.item.HomeSuggestionItem


@AndroidEntryPoint
class HomeMenuFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentHomeMenuBinding
    private var currentState: Int = BottomSheetBehavior.STATE_COLLAPSED

    private var homeData: Home? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeMenuBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>().apply {
            spanCount = 2
        }
        val layoutManager =
            GridLayoutManager(
                requireContext(),
                adapter.spanCount,
            )

        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
    }

    fun setHomeData(home: Home?) {
        if (homeData == home) return
        //(parentFragment as HomeFragment).setOnBottomSheetCallbacks(this)
        if (home == null) {
            binding.homeMenuProgressBar.show()
        } else {
            binding.homeMenuProgressBar.hide()
        }

        binding.homeMenuProgressBar.isGone = home != null

        val homeData = home ?: return
        (binding.recycler.adapter as GroupAdapter).update(homeData.menus.filter { it.slug != Menu.Slug.Media && it.slug != Menu.Slug.TrashCalendar }
            .map { homeMenu ->
                HomeMenuItem(homeMenu) {
                    findNavController().navigate(
                        when (homeMenu.slug) {
                            Menu.Slug.Notice -> HomeFragmentDirections.actionHomeFragmentToNotificationsFragment()
                            Menu.Slug.Bill -> HomeFragmentDirections.actionHomeFragmentToChargesFragment()
                            Menu.Slug.Procedure -> HomeFragmentDirections.actionHomeFragmentToAccountFragment()
                            Menu.Slug.Inquiry -> HomeFragmentDirections.actionHomeFragmentToContactFragment()
                            Menu.Slug.Faq -> HomeFragmentDirections.actionHomeFragmentToFaqFragment(
                                categoryName = null
                            )
                            Menu.Slug.Manual -> HomeFragmentDirections.actionHomeFragmentToManualFragment()
                            Menu.Slug.Benefit -> HomeFragmentDirections.actionHomeFragmentToCouponsFragment()
                            Menu.Slug.TrashCalendar -> HomeFragmentDirections.actionHomeFragmentToReminderFragment()
                            Menu.Slug.Media -> HomeFragmentDirections.actionHomeFragmentToTopicsFragment()
                        }
                    )
                }
            })

        binding.accountName.text = getString(R.string.account_building_name_format).format(
            homeData.me.building.name,
            homeData.me.room.number
        )

        this.homeData = home
    }

    companion object {
        private const val ArgHomeData = "ArgHomeData"
        fun newInstance() = HomeMenuFragment().apply {
            arguments = bundleOf()
        }
    }
}