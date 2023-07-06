package jp.co.shinoken.ui.fragment.check_form

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.co.shinoken.databinding.ItemCheckFormCategoryBinding
import jp.co.shinoken.model.CheckCategory
import jp.co.shinoken.model.CheckItem
import jp.co.shinoken.model.api.CheckForm
import jp.co.shinoken.model.api.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

class CheckFormCategoryAdapter(
    private val isSubmit: Boolean,
    checkCategory: StateFlow<List<CheckCategory>>,
    private val parentLifecycleOwner: LifecycleOwner,
    private val listener: (checkItem: CheckItem) -> Unit,
    private val removeListener: (Image, CheckItem) -> Unit,
    private val checkedListener: (CheckItem) -> Unit,
    private val textWatcher: (String, CheckItem) -> Unit,
    private val checkPointDialogListener: (String, List<CheckForm.Check>) -> Unit,
) :
    RecyclerView.Adapter<CheckFormCategoryAdapter.CheckFormCategoryViewHolder>() {
    var displayCheckItems: List<CheckCategory> = listOf()

    init {
        parentLifecycleOwner.lifecycleScope.launchWhenCreated {
            checkCategory.collect {
                displayCheckItems = it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckFormCategoryViewHolder {
        val binding = ItemCheckFormCategoryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckFormCategoryViewHolder(
            isSubmit,
            binding,
            parentLifecycleOwner,
            listener,
            removeListener,
            checkedListener,
            textWatcher,
            checkPointDialogListener
        )
    }

    override fun onBindViewHolder(holder: CheckFormCategoryViewHolder, position: Int) {
        holder.bind(
            displayCheckItems[position]
        )
    }

    override fun getItemCount(): Int {
        return displayCheckItems.size
    }

    class CheckFormCategoryViewHolder(
        private val isSubmit: Boolean,
        private val viewBinding: ItemCheckFormCategoryBinding,
        private val viewLifecycleOwner: LifecycleOwner,
        private val listener: (checkItem: CheckItem) -> Unit,
        private val removeListener: (Image, CheckItem) -> Unit,
        private val checkedListener: (CheckItem) -> Unit,
        private val textWatcher: (String, CheckItem) -> Unit,
        private val checkPointDialogListener: (String, List<CheckForm.Check>) -> Unit,
    ) :
        RecyclerView.ViewHolder(viewBinding.root) {
        lateinit var bindig: ItemCheckFormCategoryBinding

        fun bind(
            checkCategory: CheckCategory
        ) {
            this.bindig = viewBinding
            viewBinding.categoryName.text = checkCategory.name
            checkCategory.checkItems.let {
                val checkItems: StateFlow<List<CheckItem>> =
                    MutableStateFlow(it)

                viewBinding.childRecycler.apply {
                    this.adapter = CheckFormAdapter(
                        isSubmit,
                        checkItems,
                        viewLifecycleOwner,
                        listener = { selectItem ->
                            listener.invoke(selectItem)
                        },
                        removeListener = { image, removeCheckItem ->
                            removeListener.invoke(image, removeCheckItem)
                        },
                        checkedListener = { checkItem ->
                            checkedListener.invoke(checkItem)
                        },
                        textWatcher = { text, updateCheckItem ->
                            textWatcher.invoke(text, updateCheckItem)
                        },
                        checkPointDialogListener = { title, checks ->
                            checkPointDialogListener.invoke(
                                title,
                                checks
                            )
                        })
                    this.layoutManager = LinearLayoutManager(this.context)
                }
            }
        }

        fun setFormImage(checkItem: CheckItem?, position: Int?) {
            checkItem ?: return
            position ?: return
            (viewBinding.childRecycler.findViewHolderForAdapterPosition(position) as CheckFormAdapter.CheckFormItemViewHolder).setFormImage(
                checkItem
            )
        }

        fun removeImage(checkItem: CheckItem, position: Int?) {
            position ?: return
            (viewBinding.childRecycler.findViewHolderForAdapterPosition(position) as CheckFormAdapter.CheckFormItemViewHolder).removeImage(
                checkItem
            )
        }
    }
}