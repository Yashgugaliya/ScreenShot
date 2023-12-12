package com.example.screenshot.view.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.screenshot.data.model.ScreenShotEntity
import com.example.screenshot.databinding.ItemImagePagerBinding
import com.example.screenshot.util.loadImageWithCoil

class ImagePagerAdapter :
    ListAdapter<ScreenShotEntity, RecyclerView.ViewHolder>(ImagePagerComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ImagePagerViewHolder(
            ItemImagePagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ImagePagerViewHolder
        getItem(position)?.let { holder.setData(it) }
    }
}

class ImagePagerViewHolder(
    private val binding: ItemImagePagerBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun setData(item: ScreenShotEntity) {
        with(binding) {
            image.loadImageWithCoil(item.imagePath)
        }
    }

}

object ImagePagerComparator : DiffUtil.ItemCallback<ScreenShotEntity>() {
    override fun areItemsTheSame(oldItem: ScreenShotEntity, newItem: ScreenShotEntity): Boolean {
        return oldItem.imagePath == newItem.imagePath
    }

    override fun areContentsTheSame(oldItem: ScreenShotEntity, newItem: ScreenShotEntity): Boolean {
        return oldItem == newItem
    }

}
