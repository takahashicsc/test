package jp.co.shinoken.ui.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import jp.co.shinoken.databinding.ViewCheckFormSubmitDialogBinding
import jp.co.shinoken.model.api.CheckForm

class CheckFormSubmitDialog() :
    DialogFragment() {
    var isOpen = true
    private var buttonClickListener: ((Boolean) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ViewCheckFormSubmitDialogBinding.inflate(requireActivity().layoutInflater)
        binding.submitButton.setOnClickListener {
            dismiss()
            buttonClickListener?.invoke(binding.checkFormChecked.isChecked)
        }

        binding.dialogCloseButton.setOnClickListener {
            dismiss()
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

    fun setButtonClickListener(listener: (Boolean) -> Unit) {
        this.buttonClickListener = listener
    }
}
