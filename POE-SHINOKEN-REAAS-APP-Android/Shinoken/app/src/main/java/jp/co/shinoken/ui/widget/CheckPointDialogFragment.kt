package jp.co.shinoken.ui.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import jp.co.shinoken.R
import jp.co.shinoken.databinding.ViewCheckPointDialogBinding
import jp.co.shinoken.model.api.CheckForm

class CheckPointDialogFragment(val title: String, private val checks: List<CheckForm.Check>) :
    DialogFragment() {
    var isOpen = false
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ViewCheckPointDialogBinding.inflate(requireActivity().layoutInflater)
        val check = checks.first()
        binding.checkPointTitle.text = title
        binding.checkPoint.text = check.point
        binding.checkBy.text = check.by

        binding.checkPointLayout.setOnClickListener {
            isOpen = isOpen.not()
            binding.checkByLayout.isVisible = isOpen
            binding.checkPointArrow.setImageResource(
                if (isOpen) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }
}
