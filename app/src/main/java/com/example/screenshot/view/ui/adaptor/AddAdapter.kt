package com.example.screenshot.view.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.screenshot.databinding.ItemCollectionBinding
import com.example.screenshot.util.ItemClick

class AddAdapter(private val listener: ItemClick) :
    ListAdapter<String, RecyclerView.ViewHolder>(AddComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        AddViewHolder(
            ItemCollectionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as AddViewHolder
        getItem(position)?.let { holder.setData(it) }
    }
}

class AddViewHolder(
    private val binding: ItemCollectionBinding,
    private val listener: ItemClick
) : RecyclerView.ViewHolder(binding.root) {
    fun setData(item: String) {
        with(binding) {
            tvText.text = item
            root.setOnClickListener { listener(item) }
        }
    }

}

object AddComparator : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.length == newItem.length
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}
