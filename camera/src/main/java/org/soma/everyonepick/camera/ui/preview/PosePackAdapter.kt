package org.soma.everyonepick.camera.ui.preview

import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.camera.R
import org.soma.everyonepick.camera.data.entity.PosePack
import org.soma.everyonepick.camera.databinding.ItemPosePackBinding
import org.soma.everyonepick.camera.domain.model.PosePackModel

class PosePackAdapter(
    private val parentViewModel: PreviewViewModel
): ListAdapter<PosePackModel, RecyclerView.ViewHolder>(PosePackDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPosePackBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_pose_pack,
            parent,
            false
        )
        val holder = PosePackViewHolder(binding)
        binding.textName.setOnClickListener {
            parentViewModel.setSelectedPosePackIndex(holder.absoluteAdapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as PosePackViewHolder
        holder.bind(getItem(position))
    }

    class PosePackViewHolder(
        private val binding: ItemPosePackBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(posePackModel: PosePackModel) {
            binding.textName.text = posePackModel.name

            val color = if (posePackModel.isSelected) Color.WHITE else Color.GRAY
            binding.textName.setTextColor(color)
        }
    }
}

private class PosePackDiffCallback: DiffUtil.ItemCallback<PosePackModel>() {
    override fun areItemsTheSame(oldItem: PosePackModel, newItem: PosePackModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PosePackModel, newItem: PosePackModel): Boolean {
        return oldItem == newItem
    }
}