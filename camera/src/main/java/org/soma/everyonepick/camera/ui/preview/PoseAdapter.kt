package org.soma.everyonepick.camera.ui.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.soma.everyonepick.camera.R
import org.soma.everyonepick.camera.databinding.ItemPoseBinding
import org.soma.everyonepick.camera.domain.model.PoseModel
import org.soma.everyonepick.common.util.setVisibility

class PoseAdapter(
    private val parentViewModel: PreviewViewModel
): ListAdapter<PoseModel, RecyclerView.ViewHolder>(PoseDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPoseBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_pose,
            parent,
            false
        )
        val holder = PoseViewHolder(binding)
        binding.image.setOnClickListener {
            parentViewModel.onSelectPoseModel(holder.absoluteAdapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as PoseViewHolder
        holder.bind(getItem(position))
    }

    class PoseViewHolder(
        private val binding: ItemPoseBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(poseModel: PoseModel) {
            Glide.with(binding.root)
                .load(poseModel.url)
                .into(binding.image)
            binding.layoutSelected.setVisibility(poseModel.isSelected)
        }
    }
}

private class PoseDiffCallback: DiffUtil.ItemCallback<PoseModel>() {
    override fun areItemsTheSame(oldItem: PoseModel, newItem: PoseModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PoseModel, newItem: PoseModel): Boolean {
        return oldItem == newItem
    }
}