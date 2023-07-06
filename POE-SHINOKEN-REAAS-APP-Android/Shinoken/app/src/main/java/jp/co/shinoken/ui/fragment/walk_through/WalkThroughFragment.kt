package jp.co.shinoken.ui.fragment.walk_through

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentWalkThroughBinding

@AndroidEntryPoint
class WalkThroughFragment : Fragment() {

    private lateinit var binding: FragmentWalkThroughBinding
    private val viewModel: WalkThroughViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentWalkThroughBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isWalkThrough) {
            showLoginFragment()
        }

        binding.viewpager2.apply {
            val items =
                listOf(
                    R.drawable.img_walk_through1,
                    R.drawable.img_walk_through2,
                    R.drawable.img_walk_through3,
                    R.drawable.img_walk_through4
                )
            adapter = WalkThroughAdapter(items)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.backButton.isVisible = position >= 1
                    val itemCount = binding.viewpager2.adapter?.itemCount ?: 0
                    binding.nextButton.text =
                        getString(
                            if (position == (itemCount - 1)) {
                                R.string.start
                            } else {
                                R.string.next
                            }
                        )
                }
            })
        }

        binding.backButton.apply {
            val currentItem = binding.viewpager2.currentItem
            isVisible = currentItem >= 1
            setOnClickListener {
                binding.viewpager2.currentItem = binding.viewpager2.currentItem - 1
            }
        }

        binding.nextButton.apply {
            setOnClickListener {
                val itemCount = binding.viewpager2.adapter?.itemCount ?: 1
                val currentItem = binding.viewpager2.currentItem
                if (currentItem == itemCount - 1) {
                    showLoginFragment()
                    viewModel.saveWalkThrough()
                } else {
                    binding.viewpager2.currentItem = currentItem + 1
                }
            }
        }

        TabLayoutMediator(binding.indicator, binding.viewpager2) { _, _ -> }.attach()
    }

    private fun showLoginFragment() {
        findNavController().navigate(WalkThroughFragmentDirections.actionWalkThroughFragmentToLoginFragment())
    }

    companion object {
        fun newInstance() = WalkThroughFragment()
    }

    inner class WalkThroughAdapter(private val items: List<Int>) :
        RecyclerView.Adapter<PagerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
            PagerViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_walk_through, parent, false)
            )

        override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size
    }

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.walk_through_img)

        fun bind(item: Int) {
            imageView.setImageResource(item)
        }
    }
}