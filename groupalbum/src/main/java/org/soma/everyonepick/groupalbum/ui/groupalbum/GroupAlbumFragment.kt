package org.soma.everyonepick.groupalbum.ui.groupalbum

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumAdapter
import org.soma.everyonepick.groupalbum.data.GroupAlbum
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumBinding
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewModel

@AndroidEntryPoint
class GroupAlbumFragment : Fragment() {
    private var _binding: FragmentGroupalbumBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupalbumBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewModel = viewModel

        initializeRecyclerView()

        return binding.root
    }

    private fun initializeRecyclerView() {
        val adapter = GroupAlbumAdapter()
        binding.recyclerviewGroupalbum.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: GroupAlbumAdapter) {
        viewModel.groupAlbumList.observe(viewLifecycleOwner) { groupAlbumList ->
            // toMutableList(): 참조 주소를 새롭게 함으로써 갱신이 되도록 한다.
            adapter.submitList(groupAlbumList.toMutableList())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    // view model 대신 이곳에서 버튼 이벤트를 구현한 것은, 추후에는
    // 다른 페이지로 이동해서 결과값을 얻는 식으로 흐름이 짜여질 것이기 때문입니다.
    // 현재는 더미 데이터 하나를 추가하는 정도입니다.
    fun onClickCreateGroupAlbumButton() {
        val index = viewModel.groupAlbumList.value?.size?.toLong()
        viewModel.addGroupAlbum(GroupAlbum(index ?: -1, "title$index"))
    }
}