package jp.co.shinoken.ui.widget

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import jp.co.shinoken.R
import jp.co.shinoken.databinding.PopupBirthDayInputBinding
import jp.co.shinoken.model.BirthDay
import java.util.*

class BirthDayInputDialogFragment(val birthDay: BirthDay?, val listener: (BirthDay) -> Unit) :
    DialogFragment() {

    private val yearList: List<Int>
    private val monthList: List<Int>
    private lateinit var binding: PopupBirthDayInputBinding
    private val calendar = Calendar.getInstance(Locale.JAPAN)

    init {
        val year: Int = calendar.get(Calendar.YEAR)

        val yearList: MutableList<Int> = mutableListOf()
        for (i in year - 120..year) {
            yearList.add(i)
        }
        this.yearList = yearList.toList()

        val monthList: MutableList<Int> = mutableListOf()
        for (i in 1..12) {
            monthList.add(i)
        }
        this.monthList = monthList.toList()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = PopupBirthDayInputBinding.inflate(requireActivity().layoutInflater)
        val builder = AlertDialog.Builder(activity)
        activity?.let {
            binding.yearArrow.setOnClickListener {
                binding.year.performClick()
            }
            binding.year.apply {
                adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    yearList
                )
                if (birthDay?.year != null) {
                    setSelection(yearList.indexOf(birthDay.year))
                } else {
                    setSelection(yearList.indexOf(calendar.get(Calendar.YEAR)))
                }
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>, view: View,
                        position: Int, id: Long
                    ) {
                        updateDayData(
                            binding.year.selectedItem as Int,
                            binding.month.selectedItem as Int
                        )
                    }

                    override fun onNothingSelected(arg0: AdapterView<*>) {}
                }
            }

            binding.monthArrow.setOnClickListener {
                binding.month.performClick()
            }
            binding.month.apply {
                adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    monthList
                )
                if (birthDay?.month != null) {
                    setSelection(monthList.indexOf(birthDay.month))
                } else {
                    setSelection(monthList.indexOf(calendar.get(Calendar.MONTH) + 1))
                }
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>, view: View,
                        position: Int, id: Long
                    ) {
                        updateDayData(
                            binding.year.selectedItem as Int,
                            binding.month.selectedItem as Int
                        )
                    }

                    override fun onNothingSelected(arg0: AdapterView<*>) {}
                }
            }

            binding.dayArrow.setOnClickListener {
                binding.day.performClick()
            }

            binding.day.apply {
                updateDayData(
                    binding.year.selectedItem as Int?,
                    binding.month.selectedItem as Int?
                )
            }

            builder.setView(binding.root)
                .setPositiveButton("OK") { _, _ ->
                    val year = binding.year.selectedItem as Int
                    val month = binding.month.selectedItem as Int
                    val day = binding.day.selectedItem as Int
                    listener.invoke(BirthDay(year, month, day))
                }
                .setTitle("誕生日を入力")
        }
        return builder.create()
    }

    private fun updateDayData(year: Int?, month: Int?) {
        val year = year ?: return
        val month = month ?: return
        // 月によって最終日の日付が変わるため入れ直す処理
        val selectItem =
            binding.day.selectedItem as Int? ?: if (birthDay?.day != null) birthDay.day else 1

        // 閏年判定
        val isLeapYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0

        val dayListLength =
            when (month) {
                2 -> if (isLeapYear) 29 else 28
                4, 6, 9, 11 -> 30
                else -> 31
            }

        val dayList: MutableList<Int> = mutableListOf()
        for (i in 1..dayListLength) {
            dayList.add(i)
        }

        binding.day.run {
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                dayList
            )

            val selection = if (dayList.contains(selectItem)) {
                dayList.indexOf(selectItem)
            } else {
                dayList.lastIndex
            }

            setSelection(selection)
        }
    }
}