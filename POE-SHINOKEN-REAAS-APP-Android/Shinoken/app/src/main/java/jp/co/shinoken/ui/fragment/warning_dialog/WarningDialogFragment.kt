package jp.co.shinoken.ui.fragment.warning_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import jp.co.shinoken.R
import jp.co.shinoken.ui.fragment.sign_in_support.SignInSupportMailFormViewModel
import jp.co.shinoken.ui.widget.UnderLineTextView

class WarningDialogFragment(
    private val title: String,
    private val message: String,
    private val buttonText: String,
    private val underLineText: String? = null
) : DialogFragment() {

    private val viewModel: SignInSupportMailFormViewModel by viewModels()
    private var buttonClickListener: (() -> Unit)? = null
    private var underLineTextClickListener: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.view_warning_dialog, container, false)

        view.findViewById<TextView>(R.id.warning_title).apply {
            text = title
        }

        view.findViewById<TextView>(R.id.warning_message).apply {
            text = message
        }

        view.findViewById<UnderLineTextView>(R.id.warning_under_line_message).apply {
            isVisible = underLineText.isNullOrBlank().not()
            underLineText?.let {
                setUnderLineText(it)
                setOnClickListener {
                    dismiss()
                    underLineTextClickListener?.invoke()
                }
            }
        }

        view.findViewById<TextView>(R.id.warning_button_text).apply {
            text = buttonText
        }

        view.findViewById<LinearLayout>(R.id.warning_button).apply {
            setOnClickListener {
                dismiss()
                buttonClickListener?.invoke()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun setButtonClickListener(listener: () -> Unit) {
        this.buttonClickListener = listener
    }

    fun setUnderLineTextClickListener(listener: () -> Unit) {
        this.underLineTextClickListener = listener
    }
}