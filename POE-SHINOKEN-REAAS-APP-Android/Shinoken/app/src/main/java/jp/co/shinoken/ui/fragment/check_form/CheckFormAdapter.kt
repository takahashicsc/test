package jp.co.shinoken.ui.fragment.check_form

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import jp.co.shinoken.databinding.ItemCheckFormBinding
import jp.co.shinoken.model.CheckFormResult
import jp.co.shinoken.model.CheckItem
import jp.co.shinoken.model.api.CheckForm
import jp.co.shinoken.model.api.Image
import jp.co.shinoken.ui.widget.PhotoListItem
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class CheckFormAdapter(
    private val isSubmit: Boolean,
    checkItems: StateFlow<List<CheckItem>>,
    parentLifecycleOwner: LifecycleOwner,
    private val listener: (checkItem: CheckItem) -> Unit,
    private val removeListener: (Image, CheckItem) -> Unit,
    private val checkedListener: (CheckItem) -> Unit,
    private val textWatcher: (String, CheckItem) -> Unit,
    private val checkPointDialogListener: (String, List<CheckForm.Check>) -> Unit,
) :
    RecyclerView.Adapter<CheckFormAdapter.CheckFormItemViewHolder>() {
    private var displayCheckItems: List<CheckItem> = listOf()

    init {
        parentLifecycleOwner.lifecycleScope.launchWhenCreated {
            checkItems.collect {
                displayCheckItems = it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckFormItemViewHolder {
        val binding = ItemCheckFormBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckFormItemViewHolder(
            isSubmit,
            binding,
            listener,
            removeListener,
            checkedListener,
            textWatcher,
            checkPointDialogListener
        )
    }

    override fun onBindViewHolder(holder: CheckFormItemViewHolder, position: Int) {
        holder.bind(
            displayCheckItems[position]
        )
    }

    override fun getItemCount(): Int {
        return displayCheckItems.size
    }

    class CheckFormItemViewHolder(
        private val isSubmit: Boolean,
        private val viewBinding: ItemCheckFormBinding,
        private val listener: (checkItem: CheckItem) -> Unit,
        private val removeListener: (Image, CheckItem) -> Unit,
        private val checkedListener: (CheckItem) -> Unit,
        private val textWatcher: (String, CheckItem) -> Unit,
        private val checkPointDialogListener: (String, List<CheckForm.Check>) -> Unit,
    ) :
        RecyclerView.ViewHolder(viewBinding.root) {
        private lateinit var bindig: ItemCheckFormBinding

        fun bind(
            checkItem: CheckItem
        ) {
            this.bindig = viewBinding

            viewBinding.checkFormChecked.isChecked = checkItem.result == CheckFormResult.OK
            viewBinding.checkFormUnchecked.isChecked = checkItem.result == CheckFormResult.NG
            viewBinding.checkFormChecked.isEnabled =
                (checkItem.result == CheckFormResult.OK).not() && isSubmit.not()

            viewBinding.checkFormUnchecked.isEnabled =
                (checkItem.result == CheckFormResult.NG).not() && isSubmit.not()

            viewBinding.uncheckedForm.isVisible = checkItem.result == CheckFormResult.NG
            viewBinding.checkFormTitle.text = checkItem.title
            viewBinding.checkFormDescription.setText(checkItem.description)

            setUpCheckImage(isSubmit, checkItem, listener, removeListener)

            if (isSubmit.not()) {
                viewBinding.checkFormUnchecked.setOnCheckedChangeListener { _, isChecked ->
                    viewBinding.checkFormUnchecked.isChecked = isChecked
                    viewBinding.checkFormChecked.isChecked = isChecked.not()
                    viewBinding.checkFormUnchecked.isEnabled = isChecked.not() && isSubmit.not()
                    viewBinding.uncheckedForm.isVisible = isChecked
                    checkItem.result = if (isChecked) CheckFormResult.NG else CheckFormResult.OK
                    checkedListener.invoke(checkItem)
                }

                viewBinding.checkFormChecked.setOnCheckedChangeListener { _, isChecked ->
                    viewBinding.checkFormChecked.isChecked = isChecked
                    viewBinding.checkFormUnchecked.isChecked = isChecked.not()
                    viewBinding.checkFormChecked.isEnabled = isChecked.not() && isSubmit.not()
                    viewBinding.uncheckedForm.isVisible = isChecked.not()
                    checkItem.result = if (isChecked) CheckFormResult.OK else CheckFormResult.NG
                    checkedListener.invoke(checkItem)
                }
                viewBinding.checkFormDescription.addTextChangedListener(object : TextWatcher {
                    var currentLength = 0
                    var keycodeFlag: Boolean = false
                    override fun beforeTextChanged(
                        text: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        text: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (count == 0) {
                            keycodeFlag = true
                        }
                    }

                    override fun afterTextChanged(text: Editable?) {
                        if (text.toString().length < currentLength || keycodeFlag) {
                            keycodeFlag = false
                            return
                        }
                        var unfixed = false
                        val spanned = text!!.getSpans(0, text.length, Any::class.java)

                        if (spanned != null) {
                            for (obj in spanned) {
                                if (obj is android.text.style.UnderlineSpan) {
                                    unfixed = true
                                }
                            }
                        }
                        if (!unfixed) {
                            textWatcher.invoke(text.toString(), checkItem)
                        }
                    }

                })
            }

            viewBinding.checkFormDescription.apply {
                isFocusable = isSubmit.not()
                isEnabled = isSubmit.not()
                isCursorVisible = isSubmit.not()
            }

            viewBinding.checkFormHelp.setOnClickListener {
                checkPointDialogListener.invoke(checkItem.title, checkItem.checks)
            }
        }

        private fun setUpCheckImage(
            isSubmit: Boolean,
            checkItem: CheckItem,
            listener: (checkItem: CheckItem) -> Unit,
            removeListener: (Image, CheckItem) -> Unit,
        ) {
            val adapter = GroupAdapter<GroupieViewHolder>()
            if (checkItem.images.size < 3 && isSubmit.not()) {
                adapter.add(
                    PhotoListItem(
                        isSubmit = isSubmit,
                        image = null,
                        listener = { listener.invoke(checkItem) },
                        removeListener = { removeListener.invoke(it, checkItem) },
                    )
                )
            }

            checkItem.images.forEach { image ->
                adapter.add(
                    PhotoListItem(
                        isSubmit = isSubmit,
                        image = image,
                        listener = { listener.invoke(checkItem) },
                        removeListener = { removeListener.invoke(it, checkItem) }
                    )
                )
            }

            viewBinding.recycler.apply {
                this.adapter = adapter
                this.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }

        fun setFormImage(checkItem: CheckItem) {
            setUpCheckImage(
                isSubmit,
                checkItem,
                listener = listener,
                removeListener = removeListener
            )
        }

        fun removeImage(checkItem: CheckItem) {
            setUpCheckImage(
                isSubmit,
                checkItem,
                listener = listener,
                removeListener = removeListener
            )
        }
    }
}