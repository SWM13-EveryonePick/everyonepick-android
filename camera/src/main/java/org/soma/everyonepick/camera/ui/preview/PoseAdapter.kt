package org.soma.everyonepick.camera.ui.preview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.camera.R
import org.soma.everyonepick.camera.data.entity.Pose
import org.soma.everyonepick.camera.databinding.ItemPoseBinding
import org.soma.everyonepick.camera.domain.model.PoseModel
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.common_ui.util.performTouch

class PoseAdapter(
    private val viewModel: PreviewViewModel
): ListAdapter<PoseModel, PoseAdapter.PoseViewHolder>(PoseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoseViewHolder {
        val binding = getViewDataBinding<ItemPoseBinding>(parent, R.layout.item_pose)
        val holder = PoseViewHolder(binding)
        binding.onClickRoot = View.OnClickListener {
            binding.checkbox.performTouch()
        }
        binding.onClickCheckbox = View.OnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            if (item.isChecked.value) {
                viewModel.onClickPoseItem(holder.absoluteAdapterPosition)
            } else {
                viewModel.onClickPoseItem(null)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: PoseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PoseViewHolder(private val binding: ItemPoseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(poseModel: PoseModel) {
            binding.poseModel = poseModel
            binding.executePendingBindings()
        }
    }
}

private class PoseDiffCallback: DiffUtil.ItemCallback<PoseModel>() {
    override fun areItemsTheSame(oldItem: PoseModel, newItem: PoseModel): Boolean {
        return oldItem.pose.id == newItem.pose.id
    }

    override fun areContentsTheSame(oldItem: PoseModel, newItem: PoseModel): Boolean {
        return oldItem == newItem
    }
}