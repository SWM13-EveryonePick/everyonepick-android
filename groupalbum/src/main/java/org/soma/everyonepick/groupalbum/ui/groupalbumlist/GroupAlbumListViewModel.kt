package org.soma.everyonepick.groupalbum.ui.groupalbumlist

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.data.AppDatabase
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumLocalRepository
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import org.soma.everyonepick.groupalbum.domain.modellist.GroupAlbumModelList
import org.soma.everyonepick.groupalbum.domain.translator.GroupAlbumTranslator.Companion.groupAlbumLocalListToGroupAlbumModelList
import org.soma.everyonepick.groupalbum.domain.translator.GroupAlbumTranslator.Companion.groupAlbumModelListToGroupAlbumLocalList
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

/**
 * groupAlbumModelList의 값을 수정하더라도 MutableLiveData의 특성으로 인해
 * 주소값이 변하여야 observer가 작동하므로, 데이터를 변경할 때 이를 감안하여야 합니다.
 *
 * [GroupAlbumAdapter]의 설계상, 가장 마지막 아이템을 '생성 버튼'으로 취급하게 되므로 마지막 아이템에는
 * [GroupAlbumModel.dummyData]가 위치하는 것을 보장해야 하며, 이 책임은 [GroupAlbumModelList]가 지고 있습니다.
 * @see GroupAlbumAdapter
 */

@HiltViewModel
class GroupAlbumListViewModel @Inject constructor(
    private val groupAlbumUseCase: GroupAlbumUseCase,
    private val dataStoreUseCase: DataStoreUseCase,
    private val groupAlbumLocalRepository: GroupAlbumLocalRepository,
): ViewModel() {
    val groupAlbumModelList = MutableLiveData(GroupAlbumModelList())
    val isApiLoading = MutableLiveData(true)

    init {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val groupAlbumLocalList = groupAlbumLocalRepository.getGroupAlbumLocalList()
                val newGroupAlbumModelList = groupAlbumLocalList.groupAlbumLocalListToGroupAlbumModelList()
                if (groupAlbumModelList.value?.getActualItemCount() == 0) {
                    groupAlbumModelList.value?.data = newGroupAlbumModelList
                    groupAlbumModelList.postValue(groupAlbumModelList.value)
                }
            }
        }
    }

    suspend fun readGroupAlbumModelList() {
        isApiLoading.value = true

        try {
            groupAlbumModelList.value?.data = groupAlbumUseCase.readGroupAlbumModelList(dataStoreUseCase.bearerAccessToken.first()!!)
            groupAlbumModelList.value = groupAlbumModelList.value

            // 로컬 캐시
            groupAlbumModelList.value?.let {
                groupAlbumLocalRepository.resetGroupAlbumLocalList(it.data.subList(0, it.getActualItemCount()).groupAlbumModelListToGroupAlbumLocalList())
            }
        } catch (e: Exception) {}

        isApiLoading.value = false
    }

    fun deleteGroupAlbum(id: Long) {
        groupAlbumModelList.value?.removeById(id)
        groupAlbumModelList.value = groupAlbumModelList.value
    }

    fun deleteCheckedItems() {
        groupAlbumModelList.value?.removeCheckedItems()
        groupAlbumModelList.value = groupAlbumModelList.value
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        groupAlbumModelList.value?.setIsCheckboxVisible(isCheckboxVisible)
        groupAlbumModelList.value = groupAlbumModelList.value
    }

    fun checkAll() {
        groupAlbumModelList.value?.checkAll()
        groupAlbumModelList.value = groupAlbumModelList.value
    }
}