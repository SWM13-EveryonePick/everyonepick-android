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
import org.soma.everyonepick.common.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.common.util.setVisibility

class PoseAdapter(
    private val parentViewModel: PreviewViewModel
): ListAdapter<PoseModel, PoseAdapter.PoseViewHolder>(PoseDiffCallback()) {
    /**
     * 최근에 선택되었던 뷰 바인딩입니다. 특정 아이템을 클릭했을 때 해당 아이템을 클릭 처리하고 여기에 저장된 뷰 바인딩을 통해
     * 클릭 해제 처리를 합니다.
     */
    private var prevBinding: ItemPoseBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoseViewHolder {
        val binding = getViewDataBinding<ItemPoseBinding>(parent, R.layout.item_pose)
        val holder = PoseViewHolder(binding)
        binding.onClickImage = View.OnClickListener {
            // 이미 선택된 것을 또다시 클릭한 경우
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