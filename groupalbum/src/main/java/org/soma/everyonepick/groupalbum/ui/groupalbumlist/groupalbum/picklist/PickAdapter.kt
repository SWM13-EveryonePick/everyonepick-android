package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.picklist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.ItemPickBinding
import org.soma.everyonepick.groupalbum.domain.model.PickModel

class PickAdapter: ListAdapter<PickModel, RecyclerView.ViewHolder>(PickDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = getViewDataBinding<ItemPickBinding>(parent, R.layout.item_pick)
        val holder = PickViewHolder(binding)

        binding.onClickRoot = View.OnClickListener {
            // TODO: 선택 화면 or 선택 현황
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pickModel = getItem(position)
        (holder as PickViewHolder).bind(pickModel)
    }

    class PickViewHolder(private val binding: ItemPickBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pickModel: PickModel) {
            binding.pickModel = pickModel
            binding.executePendingBindings()
        }
    }
}

private class PickDiffCallback: DiffUtil.ItemCallback<PickModel>() {
    override fun areItemsTheSame(oldItem: PickModel, newItem: PickModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PickModel, newItem: PickModel): Boolean {
        return oldItem == newItem
    }
}