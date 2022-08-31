package org.soma.everyonepick.camera.ui.preview

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
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
): ListAdapter<PoseModel, PoseAdapter.PoseViewHolder>(PoseDiffCallback()) {
    private var prevBinding: ItemPoseBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoseViewHolder {
        val binding = DataBindingUtil.inflate<ItemPoseBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_pose,
            parent,
            false
        )
        val holder = PoseViewHolder(binding)
        binding.onClickImage = View.OnClickListener {
            if (binding == prevBinding) {
                binding.layoutSelected.animate().alpha(0.0f)
                prevBinding = null
                parentViewModel.setSelectedPoseIndex(null)
            } else {
                binding.layoutSelected.animate().alpha(0.5f)
                prevBinding?.layoutSelected?.animate()?.alpha(0.0f)
                prevBinding = binding
                parentViewModel.setSelectedPoseIndex(holder.absoluteAdapterPosition)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: PoseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PoseViewHolder(
        private val binding: ItemPoseBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(poseModel: PoseModel) {
            binding.poseModel = poseModel
            binding.executePendingBindings()
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