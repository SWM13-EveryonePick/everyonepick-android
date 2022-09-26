package org.soma.everyonepick.groupalbum.ui.groupalbumlist

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumLocalRepository
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import org.soma.everyonepick.groupalbum.domain.modellist.GroupAlbumModelList
import org.soma.everyonepick.groupalbum.domain.translator.GroupAlbumTranslator.Companion.toGroupAlbumModelList
import org.soma.everyonepick.groupalbum.domain.translator.GroupAlbumTranslator.Companion.toGroupAlbumLocalList
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

/**
 * [GroupAlbumAdapter]의 설계상, 가장 마지막 아이템을 '생성 버튼'으로 취급하게 되므로 마지막 아이템에는
 * [GroupAlbumModel]의 dummyData가 위치하는 것을 보장해야 하며, 이 책임은 [GroupAlbumModelList]가 지고 있습니다.
 *
 * 사용자 경험을 위해 Offline Cache를 사용하며, [groupAlbumModelList]의 값은 아래 순서를 따라 변경됩니다.
 *
 * empty -> data by Room -> data by Retrofit2
 *
 * @see GroupAlbumAdapter
 */

@HiltViewModel
class GroupAlbumListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val groupAlbumUseCase: GroupAlbumUseCase,
    private val dataStoreUseCase: DataStoreUseCase,
    private val groupAlbumLocalRepository: GroupAlbumLocalRepository,
): ViewModel() {
    private val _groupAlbumModelList = MutableStateFlow(GroupAlbumModelList(withDummyData = false))
    val groupAlbumModelList: StateFlow<GroupAlbumModelList> = _groupAlbumModelList

    private val _isApiLoading = MutableStateFlow(true)
    val isApiLoading: StateFlow<Boolean> = _isApiLoading

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    private var readJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // Offline cache 데이터 불러오기
            val newGroupAlbumModelList = groupAlbumLocalRepository.getGroupAlbumLocalList()
                .toGroupAlbumModelList()
            _groupAlbumModelList.value = GroupAlbumModelList(newGroupAlbumModelList)
        }
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        _groupAlbumModelList.value.setIsCheckboxVisible(isCheckboxVisible)
        _groupAlbumModelList.value = _groupAlbumModelList.value.getNewInstance()
    }

    fun checkAll() {
        _groupAlbumModelList.value.checkAll()
        _groupAlbumModelList.value = _groupAlbumModelList.value.getNewInstance()
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
                        .toGroupAlbumLocalList()
                    groupAlbumLocalRepository.resetGroupAlbumLocalList(groupAlbumLocalList)
                }
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_read_group_album)
            }

            _isApiLoading.value = false
        }
    }

    fun leaveCheckedGroupAlbum() {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                getCheckedGroupAlbumIdList().forEach {
                    groupAlbumUseCase.leaveGroupAlbum(token, it!!)
                }
                readGroupAlbumModelList()
            } catch (e: Exception) {
                readGroupAlbumModelList()
                _toastMessage.value = context.getString(R.string.toast_failed_to_exit_group_album)
            }
        }
    }

    private fun getCheckedGroupAlbumIdList() = _groupAlbumModelList.value.getActualData()
        .filter { it.isChecked.value }
        .map { it.groupAlbum.id }
}