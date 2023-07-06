package jp.co.shinoken.ui.fragment.check_form

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.R
import jp.co.shinoken.databinding.FragmentCheckFormBinding
import jp.co.shinoken.extension.appError
import jp.co.shinoken.extension.convertUriToFile
import jp.co.shinoken.extension.showSnackBar
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.ui.fragment.contract_update.ContractUpdateFragment
import jp.co.shinoken.ui.widget.CheckFormSubmitDialog
import jp.co.shinoken.ui.widget.CheckPointDialogFragment
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckFormFragment : Fragment() {

    private lateinit var binding: FragmentCheckFormBinding
    private val viewModel: CheckFormViewModel by viewModels()

    private lateinit var listener: ICheckFormFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCheckFormBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()
        binding.recycler.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(context)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.formCheckItem.collect {
                binding.recycler.apply {
                    this.adapter = CheckFormCategoryAdapter(
                        viewModel.isSubmit,
                        viewModel.formCheckItem,
                        viewLifecycleOwner,
                        listener = { selectItem ->
                            selectImage()
                            viewModel.checkItemSlug = selectItem.slug
                        },
                        removeListener = { image, removeCheckItem ->
                            viewModel.removeImage(image, removeCheckItem)
                            val removeCheckCategory =
                                viewModel.formCheckItem.value.firstOrNull { checkCategory ->
                                    checkCategory.checkItems.firstOrNull { it == removeCheckItem } != null
                                }

                            val index: Int = viewModel.formCheckItem.value.map { checkCategory ->
                                checkCategory.checkItems.indexOf(
                                    removeCheckCategory?.checkItems?.firstOrNull { it == removeCheckItem }
                                )
                            }.first()

                            if (removeCheckCategory != null) {
                                val position =
                                    viewModel.formCheckItem.value.indexOf(removeCheckCategory)
                                (findViewHolderForAdapterPosition(position) as CheckFormCategoryAdapter.CheckFormCategoryViewHolder).removeImage(
                                    removeCheckItem,
                                    index
                                )
                            }
                        },
                        checkedListener = { checkItem ->
                            viewModel.updateCheck(checkItem)
                        },
                        textWatcher = { text, updateCheckItem ->
                            viewModel.updateText(text, updateCheckItem)
                        },
                        checkPointDialogListener = { title, checks ->
                            CheckPointDialogFragment(
                                title = title,
                                checks = checks
                            ).show(
                                requireActivity().supportFragmentManager,
                                CheckFormFragment::class.java.simpleName
                            )
                        }
                    )
                    this.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.deadline.collect {
                binding.checkFormDeadline.text =
                    getString(R.string.check_form_deadline_format).format(if (it.isNullOrEmpty()) "" else it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.appError.collect {
                it ?: return@collect
                appError(it) { viewModel.fetch() }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.putAppError.collect {
                it ?: return@collect
                appError(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isButtonEnable.collect {
                binding.checkFormSend.isEnabled = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collect {
                binding.checkFormProgressBar.hide()
                viewModel.validation()
                when (it) {
                    ApiState.Empty -> return@collect
                    ApiState.Success -> {
                        showSnackBar(
                            binding.checkFormSend,
                            getString(R.string.check_form_submit_success_message)
                        )
                    }
                    ApiState.LOADING -> {
                        binding.checkFormProgressBar.show()
                    }
                }
            }
        }

        binding.checkFormSend.setOnClickListener {

            CheckFormSubmitDialog(
            ).apply {
                setButtonClickListener {
                    viewModel.postData(it)
                }
            }.show(
                requireActivity().supportFragmentManager,
                ContractUpdateFragment::class.java.simpleName
            )
        }

        if (viewModel.isCheckedForm.not()) {
            viewModel.fetch()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ICheckFormFragment) {
            listener = context
        }
    }

    private fun selectImage() {
        listener.selectImageUri()
    }

    fun setImage(uri: Uri) {
        val updateCheckCategory =
            viewModel.formCheckItem.value.firstOrNull { checkCategory ->
                checkCategory.checkItems.firstOrNull { it.slug == viewModel.checkItemSlug } != null
            }

        updateCheckCategory ?: return

        lifecycleScope.launch {
            val file = convertUriToFile(uri)
            val setFormImage = async { viewModel.setFormImages(file) }
            setFormImage.await()

            val index: Int = viewModel.formCheckItem.value.map { checkCategory ->
                checkCategory.checkItems.indexOf(
                    updateCheckCategory.checkItems.firstOrNull { it.slug == viewModel.checkItemSlug }
                )
            }.first()

            val position = viewModel.formCheckItem.value.indexOf(updateCheckCategory)
            (binding.recycler.findViewHolderForAdapterPosition(position) as CheckFormCategoryAdapter.CheckFormCategoryViewHolder).setFormImage(
                updateCheckCategory.checkItems.firstOrNull { it.slug == viewModel.checkItemSlug },
                index
            )
        }
    }

    interface ICheckFormFragment {
        fun selectImageUri()
    }

    companion object {
        const val ArgIsCheckedForm = "ArgIsCheckedForm"
        const val ArgCheckFormData = "ArgCheckFormData"
        fun newInstance() = CheckFormFragment()
    }
}