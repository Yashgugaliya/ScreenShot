package com.example.screenshot.view.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.screenshot.data.model.ScreenShotEntity
import com.example.screenshot.databinding.ItemImageBinding
import com.example.screenshot.util.ImageClick
import com.example.screenshot.util.loadImageWithCoil

class ImageAdapter(private val listener: ImageClick) :
    ListAdapter<ScreenShotEntity, RecyclerView.ViewHolder>(ImageComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ImageViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ImageViewHolder
        getItem(position)?.let { holder.setData(it, position) }
    }
}

class ImageViewHolder(
    private val binding: ItemImageBinding, private val listener: ImageClick
) : RecyclerView.ViewHolder(binding.root) {
    fun setData(item: ScreenShotEntity, po: Int) {
        with(binding) {
            image.loadImageWithCoil(item.imagePath)
            root.setOnClickListener {
                listener(item, po)
            }
        }
    }

}

object ImageComparator : DiffUtil.ItemCallback<ScreenShotEntity>() {
    override fun areItemsTheSame(oldItem: ScreenShotEntity, newItem: ScreenShotEntity): Boolean {
        return oldItem.imagePath == newItem.imagePath
    }

    override fun areContentsTheSame(oldItem: ScreenShotEntity, newItem: ScreenShotEntity): Boolean {
        return oldItem == newItem
    }

}
