package com.example.screenshot.view.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.screenshot.R
import com.example.screenshot.databinding.ItemCollectionBinding
import com.example.screenshot.util.ItemClick

class CollectionAdapter(private val listener: ItemClick, private val isEnable: Boolean) :
    ListAdapter<String, RecyclerView.ViewHolder>(CollectionComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CollectionViewHolder(
            ItemCollectionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener, isEnable
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as CollectionViewHolder
        getItem(position)?.let { holder.setData(it) }
    }
}

class CollectionViewHolder(
    private val binding: ItemCollectionBinding,
    private val listener: ItemClick,
    private val boolean: Boolean
) : RecyclerView.ViewHolder(binding.root) {
    fun setData(item: String) {
        with(binding) {
            tvText.text = item
            if (boolean)
                tvText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            else {
                tvText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0)
                root.setOnClickListener { listener(item) }
            }
        }
    }
}

object CollectionComparator : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.length == newItem.length
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
