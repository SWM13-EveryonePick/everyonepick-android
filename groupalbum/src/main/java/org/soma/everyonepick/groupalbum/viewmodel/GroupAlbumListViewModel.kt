package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import org.soma.everyonepick.common.data.pref.PreferencesDataStore
import org.soma.everyonepick.groupalbum.adapter.groupalbum.GroupAlbumAdapter
import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.data.itemlist.GroupAlbumItemList
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository
import javax.inject.Inject

/**
 * groupAlbumItemList의 값을 수정하더라도 MutableLiveData의 특성으로 인해
 * 주소값이 변하여야 observer가 작동하므로, 데이터를 변경할 때 이를 감안하여야 합니다.
 *
 * [GroupAlbumAdapter]의 설계상, 가장 마지막 아이템을 '생성 버튼'으로 취급하게 되므로 마지막 아이템에는
 * [GroupAlbumItem.dummyData]가 위치하는 것을 보장해야 하며, 이 책임은 [GroupAlbumItemList]가 지고 있습니다.
 * @see GroupAlbumAdapter
 */

@HiltViewModel
class GroupAlbumListViewModel @Inject constructor(
    private val groupAlbumRepository: GroupAlbumRepository,
    private val preferencesDataStore: PreferencesDataStore
): ViewModel() {
    val groupAlbumItemList = MutableLiveData(GroupAlbumItemList())
    val isApiLoading = MutableLiveData(true)

    suspend fun fetchGroupAlbumItemList() {
        isApiLoading.value = true

        try {
            groupAlbumItemList.value?.data = groupAlbumRepository.getGroupAlbumItemList(preferencesDataStore.accessToken.first()!!)
            groupAlbumItemList.value = groupAlbumItemList.value
        } catch (e: Exception) {}

        isApiLoading.value = false
    }

    fun deleteGroupAlbum(id: Long) {
        groupAlbumItemList.value?.removeById(id)
        groupAlbumItemList.value = groupAlbumItemList.value
    }

    fun deleteCheckedItems() {
        groupAlbumItemList.value?.removeCheckedItems()
        groupAlbumItemList.value = groupAlbumItemList.value
    }


    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        groupAlbumItemList.value?.setIsCheckboxVisible(isCheckboxVisible)
        groupAlbumItemList.value = groupAlbumItemList.value
    }

    fun checkAll() {
        groupAlbumItemList.value?.checkAll()
        groupAlbumItemList.value = groupAlbumItemList.value
    }
}