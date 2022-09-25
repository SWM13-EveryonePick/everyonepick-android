package org.soma.everyonepick.camera.ui.preview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.camera.R
import org.soma.everyonepick.camera.databinding.ItemPoseBinding
import org.soma.everyonepick.camera.domain.model.PoseModel
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding

class PoseAdapter(
    private val parentViewModel: PreviewViewModel
): ListAdapter<PoseModel, PoseAdapter.PoseViewHolder>(PoseDiffCallback()) {
    /**
     * 최근에 선택한 뷰 바인딩입니다. 아이템을 클릭할 때 기존에 선택된 아이템을 선택 해제 처리해야 하는데, 이를 위해 사용됩니다.
     */
    private var prevBinding: ItemPoseBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoseViewHolder {
        val binding = getViewDataBinding<ItemPoseBinding>(parent, R.layout.item_pose)
        val holder = PoseViewHolder(binding)
        binding.onClickImage = View.OnClickListener {
            // 선택했던 아이템을 또다시 클릭한 경우
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

    class PoseViewHolder(private val binding: ItemPoseBinding): RecyclerView.ViewHolder(binding.root) {
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