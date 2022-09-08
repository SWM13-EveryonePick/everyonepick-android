package org.soma.everyonepick.groupalbum.ui.groupalbumlist

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
 * [GroupAlbumAdapter]의 설계상, 가장 마지막 아이템을 '생성 버튼'으로 취급하게 되므로 마지막 아이템에는
 * [GroupAlbumModel.dummyData]가 위치하는 것을 보장해야 하며, 이 책임은 [GroupAlbumModelList]가 지고 있습니다.
 *
 * 사용자 경험을 위해 Offline Cache를 사용하며, [groupAlbumModelList]의 값은 아래 순서를 따라 변경됩니다.
 *
 * empty -> data by Room -> data by Retrofit2
 *
 * @see GroupAlbumAdapter
 */

@HiltViewModel
class GroupAlbumListViewModel @Inject constructor(
    private val groupAlbumUseCase: GroupAlbumUseCase,
    private val dataStoreUseCase: DataStoreUseCase,
    private val groupAlbumLocalRepository: GroupAlbumLocalRepository,
): ViewModel() {
    private val _groupAlbumModelList = MutableStateFlow(GroupAlbumModelList(withDummyData = false))
    val groupAlbumModelList: StateFlow<GroupAlbumModelList> = _groupAlbumModelList

    private val _isApiLoading = MutableStateFlow(true)
    val isApiLoading: StateFlow<Boolean> = _isApiLoading

    private var readJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // Offline cache 데이터 불러오기
            val newGroupAlbumModelList = groupAlbumLocalRepository.getGroupAlbumLocalList()
                .groupAlbumLocalListToGroupAlbumModelList()
            _groupAlbumModelList.value = GroupAlbumModelList(newGroupAlbumModelList)
        }
    }

    fun readGroupAlbumModelList() {
        readJob?.cancel()

        readJob = viewModelScope.launch {
            _isApiLoading.value = true
            try {
                val newGroupAlbumModelList = groupAlbumUseCase.readGroupAlbumModelList(dataStoreUseCase.bearerAccessToken.first()!!)
                if (_groupAlbumModelList.value.data != newGroupAlbumModelList) {
                    _groupAlbumModelList.value = GroupAlbumModelList(newGroupAlbumModelList)

                    // Offline cache를 위해 데이터 저장
                    val groupAlbumLocalList = _groupAlbumModelList.value.getActualData()
                        .groupAlbumModelListToGroupAlbumLocalList()
                    groupAlbumLocalRepository.resetGroupAlbumLocalList(groupAlbumLocalList)
                }
            } catch (e: Exception) {}

            _isApiLoading.value = false
        }
    }

    fun getCheckedItemList() = _groupAlbumModelList.value.getCheckedItemIdList()

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        _groupAlbumModelList.value.setIsCheckboxVisible(isCheckboxVisible)
        _groupAlbumModelList.value = _groupAlbumModelList.value.getNewInstance()
    }

    fun checkAll() {
        _groupAlbumModelList.value.checkAll()
        _groupAlbumModelList.value = _groupAlbumModelList.value.getNewInstance()
    }
}