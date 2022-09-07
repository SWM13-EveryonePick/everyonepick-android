package org.soma.everyonepick.groupalbum.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.GroupAlbumListViewModel
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumViewModel
import org.soma.everyonepick.groupalbum.util.testGroupAlbumModelList
import org.soma.everyonepick.groupalbum.util.testGroupAlbumReadDetail
import javax.inject.Inject

@HiltAndroidTest
class GroupAlbumViewModelTest {
    private lateinit var viewModel: GroupAlbumViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)

    @Inject lateinit var dataStoreUseCase: DataStoreUseCase
    @Inject lateinit var userUseCase: UserUseCase
    @Inject lateinit var groupAlbumUseCase: GroupAlbumUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = GroupAlbumViewModel(
            SavedStateHandle(),
            dataStoreUseCase,
            userUseCase,
            groupAlbumUseCase
        )
        viewModel.setGroupAlbum(testGroupAlbumReadDetail)
    }
}
