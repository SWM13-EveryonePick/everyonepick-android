package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.photo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _photoUrl = MutableStateFlow(savedStateHandle.get<String>("photoUrl")?: "")
    val photoUrl: StateFlow<String?> = _photoUrl
}