package org.soma.everyonepick.camera.ui.preview

import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.camera.R
import org.soma.everyonepick.camera.data.entity.PosePack
import org.soma.everyonepick.camera.databinding.ItemPoseBinding
import org.soma.everyonepick.camera.databinding.ItemPosePackBinding
import org.soma.everyonepick.camera.domain.model.PosePackModel
import org.soma.everyonepick.common.util.performTouch

class PosePackAdapter(
    private val parentViewModel: PreviewViewModel
): ListAdapter<PosePackModel, PosePackAdapter.PosePackViewHolder>(PosePackDiffCallback()) {
    /**
     * 최근에 선택되었던 뷰 바인딩입니다. 특정 아이템을 클릭했을 때 해당 아이템을 클릭 처리하고 여기에 저장된 뷰 바인딩을 통해
     * 클릭 해제 처리를 합니다.
     */
    private var prevBinding: ItemPosePackBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosePackViewHolder {
        val binding = DataBindingUtil.inflate<ItemPosePackBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_pose_pack,
            parent,
            false
        )
        val holder = PosePackViewHolder(binding)
        binding.onClickText = View.OnClickListener {
            prevBinding?.textName?.setTextColor(Color.GRAY)
            binding.textName.setTextColor(Color.WHITE)

            prevBinding = binding
            parentViewModel.setSelectedPosePackIndex(holder.absoluteAdapterPosition)
        }

        return holder
    }

    override fun onBindViewHolder(holder: PosePackViewHolder, position: Int) {
        // 최초에 디폴트로 지정된 인덱스 클릭 처리
        if (holder.absoluteAdapterPosition == parentViewModel.selectedPosePackIndex.value) {
            holder.getBinding().textName.performTouch()
        }

        holder.bind(getItem(position))
    }

    class PosePackViewHolder(
        private val binding: ItemPosePackBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(posePackModel: PosePackModel) {
            binding.posePackModel = posePackModel
            binding.executePendingBindings()
        }

        fun getBinding() = binding
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