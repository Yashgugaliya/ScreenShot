package com.example.screenshot.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.screenshot.data.model.ScreenShotEntity
import com.example.screenshot.databinding.FragmentShareBinding
import com.example.screenshot.util.LoaderView
import com.example.screenshot.util.ScreenState
import com.example.screenshot.util.showToast
import com.example.screenshot.view.ui.adaptor.ImageAdapter
import com.example.screenshot.view.ui.adaptor.ImagePagerAdapter
import com.example.screenshot.viewmodel.ScreenShotViewModel

class ShareFragment : Fragment() {

    private lateinit var binding: FragmentShareBinding
    private val groupAdapter = ImageAdapter(::imageClick)
    private val imagePagerAdapter = ImagePagerAdapter()
    private lateinit var loaderView: LoaderView
    private val viewModel: ScreenShotViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderView = LoaderView(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShareBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvList.adapter = groupAdapter
        binding.ivMain.adapter = imagePagerAdapter

        viewModel.screenshots.observe(viewLifecycleOwner) { screenshots ->
            when (screenshots) {
                is ScreenState.Loading -> {
                    // Handle loading state
                    loaderView.showLoading()
                }

                is ScreenState.Success -> {
                    groupAdapter.submitList(screenshots.data)
                    imagePagerAdapter.submitList(screenshots.data)
                    viewModel.data.value = screenshots.data[0]
                    loaderView.hideLoading()
                }

                is ScreenState.Error -> {
                    // Handle error state
                    loaderView.hideLoading()
                    requireContext().showToast("Something Went Wrong!")
                }
            }
        }

        binding.ivMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.data.value = imagePagerAdapter.currentList[position]
            }
        })
    }

    private fun imageClick(item: ScreenShotEntity, i: Int) {
        binding.ivMain.setCurrentItem(i, true)
        viewModel.data.value = item
    }

    companion object {
        fun newInstance() = ShareFragment()
    }
}