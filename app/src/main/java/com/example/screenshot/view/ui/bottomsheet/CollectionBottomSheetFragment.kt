package com.example.screenshot.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.screenshot.R
import com.example.screenshot.databinding.FragmentCollectionBottomSheetBinding
import com.example.screenshot.util.LoaderView
import com.example.screenshot.util.ScreenState
import com.example.screenshot.util.showToast
import com.example.screenshot.view.ui.adaptor.AddAdapter
import com.example.screenshot.view.ui.adaptor.CollectionAdapter
import com.example.screenshot.viewmodel.ScreenShotViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CollectionBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCollectionBottomSheetBinding
    private val viewModel: ScreenShotViewModel by activityViewModels()
    private var selectedLabel = arrayListOf<String>()
    private var label = arrayListOf<String>()
    private val collectionAdapter = CollectionAdapter(::listClick, false)
    private val addAdapter = AddAdapter(::addClick)
    private lateinit var loaderView: LoaderView
    private var isFresh = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
        loaderView = LoaderView(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCollectionBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedFlex = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }

        val addFlex = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }

        val item = viewModel.data.value

        if (item != null) {
            selectedLabel.addAll(item.selectedLabel.filter { it.isNotEmpty() })
            label.addAll(item.labels)
        }
        binding.rvSelected.apply {
            layoutManager = selectedFlex
            adapter = collectionAdapter
        }
        binding.rvAdd.apply {
            layoutManager = addFlex
            adapter = addAdapter
        }
        addAdapter.submitList(item?.labels)
        collectionAdapter.submitList(item?.selectedLabel?.filter { it.isNotEmpty() })

        binding.tvSaveList.setOnClickListener {
            item?.let {
                viewModel.updateScreenshotCollection(it.id, selectedLabel, label)
                isFresh = true
            }
        }

        viewModel.updateCollection.observe(viewLifecycleOwner) { screenShot ->
            when (screenShot) {
                is ScreenState.Loading -> {
                    // Handle loading state
                    loaderView.showLoading()
                }

                is ScreenState.Success -> {
                    if (isFresh) {
                        viewModel.data.value = screenShot.data
                        viewModel.getUpdatedScreenShots()
                        dismiss()
                    }
                    loaderView.hideLoading()
                }

                is ScreenState.Error -> {
                    // Handle error state
                    loaderView.hideLoading()
                    requireContext().showToast("Something Went Wrong!")
                }
            }
        }
    }

    private fun listClick(lab: String) {
        selectedLabel.remove(lab)
        label.add(lab)
        collectionAdapter.submitList(selectedLabel)
        addAdapter.submitList(label)
        collectionAdapter.notifyDataSetChanged()
        addAdapter.notifyDataSetChanged()
    }

    private fun addClick(lab: String) {
        label.remove(lab)
        selectedLabel.add(lab)
        collectionAdapter.submitList(selectedLabel)
        addAdapter.submitList(label)
        collectionAdapter.notifyDataSetChanged()
        addAdapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance(): CollectionBottomSheetFragment {
            return CollectionBottomSheetFragment()
        }
    }
}