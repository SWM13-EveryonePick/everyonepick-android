package org.soma.everyonepick.camera.ui.preview

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.camera.R
import org.soma.everyonepick.camera.databinding.ItemPosePackBinding
import org.soma.everyonepick.camera.domain.model.PosePackModel
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.common_ui.util.performTouch

class PosePackAdapter(
    private val viewModel: PreviewViewModel
): ListAdapter<PosePackModel, PosePackAdapter.PosePackViewHolder>(PosePackDiffCallback()) {
    /**
     * 최근에 선택한 뷰 바인딩입니다. 아이템을 클릭할 때 기존에 선택된 아이템을 선택 해제 처리해야 하는데, 이를 위해 사용됩니다.
     */
    private var prevBinding: ItemPosePackBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosePackViewHolder {
        val binding = getViewDataBinding<ItemPosePackBinding>(parent, R.layout.item_pose_pack)
        val holder = PosePackViewHolder(binding)
        binding.onClickText = View.OnClickListener {
            prevBinding?.textName?.setTextColor(Color.GRAY)
            binding.textName.setTextColor(Color.WHITE)

            prevBinding = binding
            viewModel.setCurrentPosePackIndex(holder.absoluteAdapterPosition)
        }

        return holder
    }

    override fun onBindViewHolder(holder: PosePackViewHolder, position: Int) {
        // 기본으로 선택되어 있어야 할 아이템 클릭 처리
        if (holder.absoluteAdapterPosition == viewModel.selectedPosePackIndex.value) {
            holder.getBinding().textName.performTouch()
        }

        holder.bind(getItem(position))
    }

    class PosePackViewHolder(private val binding: ItemPosePackBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(posePackModel: PosePackModel) {
            binding.posePackModel = posePackModel
            binding.executePendingBindings()
        }

        fun getBinding() = binding
    }
}

private class PosePackDiffCallback: DiffUtil.ItemCallback<PosePackModel>() {
    override fun areItemsTheSame(oldItem: PosePackModel, newItem: PosePackModel): Boolean {
        return oldItem.peopleNum == newItem.peopleNum
    }

    override fun areContentsTheSame(oldItem: PosePackModel, newItem: PosePackModel): Boolean {
        return oldItem == newItem
    }
}