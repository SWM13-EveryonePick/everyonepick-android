package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.resultphotolist

import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.common_ui.util.performTouch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.ItemResultPhotoBinding
import org.soma.everyonepick.groupalbum.domain.model.ResultPhotoModel
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumFragmentDirections
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumViewModel
import org.soma.everyonepick.groupalbum.util.SelectionMode

class ResultPhotoAdapter(
    private val groupAlbumViewModel: GroupAlbumViewModel
): androidx.recyclerview.widget.ListAdapter<ResultPhotoModel, RecyclerView.ViewHolder>(ResultPhotoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = getViewDataBinding<ItemResultPhotoBinding>(parent, R.layout.item_result_photo)
        val holder = ResultPhotoViewHolder(binding)

        binding.onClickRoot = View.OnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            // 일반 모드일 때
            if (!item.isCheckboxVisible) {
                val directions = GroupAlbumFragmentDirections.toPhotoFragment(
                    groupAlbumViewModel.groupAlbum.value.id?: -1,
                    -1,
                    item.resultPhoto.id,
                    item.resultPhoto.resultPhotoUrl
                )
                binding.root.findNavController().navigate(directions)
            } else {
                binding.checkbox.performTouch()
            }
        }

        binding.onLongClickRoot = View.OnLongClickListener {
            groupAlbumViewModel.setResultPhotoSelectionMode(SelectionMode.SELECTION_MODE)
            true
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val resultPhotoModel = getItem(position)
        (holder as ResultPhotoViewHolder).bind(resultPhotoModel)
    }

    class ResultPhotoViewHolder(private val binding: ItemResultPhotoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(resultPhotoModel: ResultPhotoModel) {
            binding.resultPhotoModel = resultPhotoModel
            binding.executePendingBindings()
        }
    }
}

class ResultPhotoDiffCallback: DiffUtil.ItemCallback<ResultPhotoModel>() {
    override fun areContentsTheSame(oldItem: ResultPhotoModel, newItem: ResultPhotoModel): Boolean {
        return oldItem.resultPhoto.id == newItem.resultPhoto.id
    }

    override fun areItemsTheSame(oldItem: ResultPhotoModel, newItem: ResultPhotoModel): Boolean {
        return oldItem == newItem
    }
}