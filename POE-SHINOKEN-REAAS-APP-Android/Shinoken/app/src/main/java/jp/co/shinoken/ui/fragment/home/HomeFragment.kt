package jp.co.shinoken.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentHomeBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.convertDpToPx
import kotlinx.coroutines.flow.collect
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var binding: FragmentHomeBinding
    private var bottomSheetBehavior: BottomSheetBehavior<View?>? = null

    private var savedViewInstance: View? = null

    private var isReload = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return if (savedViewInstance != null) {
            savedViewInstance!!
        } else {
            isReload = arguments?.getBoolean("is_reload") ?: false
            FragmentHomeBinding.inflate(inflater, container, false).also {
                binding = it
            }.root
            savedViewInstance = binding.root
            savedViewInstance!!

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBackdrop()
        lifecycleScope.launchWhenCreated {
            viewModel.homeData.collect {
                it ?: return@collect
                val currentFragment =
                    requireActivity().supportFragmentManager.findFragmentById(R.id.home_menu_fragment)
                if (currentFragment is HomeMenuFragment) {
                    currentFragment.setHomeData(
                        it
                    )
                }

                // Weatherがからの場合表示できないので非表示
                if (it.weather.weathers.isEmpty().not()) {
                    binding.weatherImg.isVisible = true
                    binding.weatherImg.setImageResource(it.weather.weathers.first().groupCode.iconRes)
                    val temperatures = it.weather.temperatures
                    binding.weatherTemperatures.text =
                        getString(R.string.weather_temperatures_format).format(
                            temperatures.first { temperature -> temperature.slug == "max" }.value,
                            temperatures.first { temperature -> temperature.slug == "min" }.value
                        )
                }

                it.me.icon?.publishUrl?.let { iconUrl ->
                    binding.profileImg.load("$iconUrl?${Date()}") {
                        memoryCachePolicy(CachePolicy.DISABLED)
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.messageRes.collect {
                it ?: return@collect
                binding.homeHeadMessage.text = getString(it)
            }
        }


        lifecycleScope.launchWhenCreated {
            viewModel.isSwitchAccount.collect {
                binding.accountSwitchButton.isVisible = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it) { viewModel.fetch() }
            }
        }

        binding.accountSwitchButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAccountSwitchFragment())
        }

        binding.settingButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingFragment())
        }

//        binding.searchLayout.isGone =
//            bottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED
//
//
//        binding.homeAccountLayout.isGone =
//            bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED
        binding.searchLayout.visibility = View.GONE
        binding.homeAccountLayout.visibility = View.VISIBLE
    }

    fun closeBottomSheet() {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun openBottomSheet() {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun configureBackdrop() {
        val currentFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.home_menu_fragment)
        if (currentFragment !is HomeMenuFragment || isReload) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.home_menu_fragment,
                    HomeMenuFragment.newInstance()
                )
                .commit()
            isReload = false
        }

        BottomSheetBehavior.from((binding.homeMenuFragment as View)).let { bs ->
            bs.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    onBottomStateChance(newState)
                }
            })

            val viewTreeObserver = binding.homeAccountLayout.viewTreeObserver
            viewTreeObserver.addOnGlobalLayoutListener {
                val layoutHeight =
                    if (binding.homeAccountLayout.isGone) binding.searchLayout.height else binding.homeAccountLayout.height
                val mainLayoutHeight = binding.main.height
                val maxHeight =
                    mainLayoutHeight - (layoutHeight + 48.convertDpToPx(requireContext())).toInt()
                bs.peekHeight = maxHeight

                val heightMargin = (if (binding.homeAccountLayout.isGone) {
                    binding.searchLayout.y + 16.convertDpToPx(requireContext())
                } else {
                    binding.profileImg.y + 32.convertDpToPx(requireContext())
                }).toInt()

                val height = binding.main.height - heightMargin

                binding.homeMenuFragment.layoutParams.height = height
            }
            bottomSheetBehavior = bs
        }
        binding.searchLayout.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        binding.searchLayout.isGone =
//            bottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED
//
//        binding.homeAccountLayout.isGone =
//            bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED
    }

    private fun onBottomStateChance(newState: Int) {
        binding.homeAccountLayout.isGone = newState == BottomSheetBehavior.STATE_EXPANDED
        binding.searchLayout.isVisible = newState == BottomSheetBehavior.STATE_EXPANDED
        binding.homeAccountLayout.visibility = View.VISIBLE
        binding.searchLayout.visibility = View.GONE
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}