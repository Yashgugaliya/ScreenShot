package com.example.screenshot.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.screenshot.databinding.FragmentInfoBinding
import com.example.screenshot.util.LoaderView
import com.example.screenshot.util.ScreenState
import com.example.screenshot.util.loadImageWithCoil
import com.example.screenshot.util.showToast
import com.example.screenshot.view.ui.adaptor.CollectionAdapter
import com.example.screenshot.view.ui.bottomsheet.CollectionBottomSheetFragment
import com.example.screenshot.viewmodel.ScreenShotViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    private val viewModel: ScreenShotViewModel by activityViewModels()
    private val collectionAdapter = CollectionAdapter(::listClick, true)
    private lateinit var loaderView: LoaderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderView = LoaderView(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = viewModel.data.value

        val flexboxLayoutManager = FlexboxLayoutManager(requireContext())
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        //flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START

        binding.apply {
            ivMain.loadImageWithCoil(item?.imagePath)
            if (!item?.note.isNullOrEmpty())
                tvNote.setText(item?.note)
            tvDescription.text = item?.description
            tvSave.setOnClickListener {
                item?.id?.let { it1 -> viewModel.updateScreenshotNote(it1, tvNote.text.toString()) }
                tvNote.clearFocus()
            }
            rvCollection.apply {
                layoutManager = flexboxLayoutManager
                adapter = collectionAdapter
            }
            collectionAdapter.submitList(item?.selectedLabel?.filter { it.isNotEmpty() })
            tvEdit.setOnClickListener {
                CollectionBottomSheetFragment.newInstance().show(childFragmentManager, "CollectionBottomSheetFragment")
            }
        }

        viewModel.screenshotUpdate.observe(viewLifecycleOwner) { screenShot ->
            when (screenShot) {
                is ScreenState.Loading -> {
                    // Handle loading state
                    loaderView.showLoading()
                }

                is ScreenState.Success -> {
                    viewModel.getUpdatedScreenShots()
                    viewModel.data.value = screenShot.data
                    runBlocking { delay(500) }
                    loaderView.hideLoading()
                }

                is ScreenState.Error -> {
                    // Handle error state
                    loaderView.hideLoading()
                    requireContext().showToast("Something Went Wrong!")
                }
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { ssUpdate ->
            binding.tvNote.setText(ssUpdate?.note)
            binding.tvNote.clearFocus()
            collectionAdapter.submitList(ssUpdate?.selectedLabel?.filter { it.isNotEmpty() })
            collectionAdapter.notifyDataSetChanged()
        }

    }

    private fun listClick(s: String) {}

    companion object {
        @JvmStatic
        fun newInstance(): InfoFragment {
            return InfoFragment()
        }
    }
}


