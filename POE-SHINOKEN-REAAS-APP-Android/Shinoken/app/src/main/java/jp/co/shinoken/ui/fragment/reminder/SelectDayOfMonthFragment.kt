package jp.co.shinoken.ui.fragment.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.databinding.FragmentSelectDayOfWeekBinding

@AndroidEntryPoint
class SelectDayOfMonthFragment : Fragment() {

    private lateinit var binding: FragmentSelectDayOfWeekBinding
    private lateinit var viewModel: SelectDayOfMonthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSelectDayOfWeekBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    companion object {
        fun newInstance() = SelectDayOfMonthFragment()
    }
}