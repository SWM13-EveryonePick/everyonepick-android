package org.soma.everyonepick.groupalbum.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import org.soma.everyonepick.groupalbum.util.testGroupAlbumModel
import org.soma.everyonepick.groupalbum.util.testGroupAlbumModelList
import javax.inject.Inject

@HiltAndroidTest
class GroupAlbumListViewModelTest {
    private lateinit var viewModel: GroupAlbumListViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)

    @Inject lateinit var groupAlbumUseCase: GroupAlbumUseCase
    @Inject lateinit var dataStoreUseCase: DataStoreUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = GroupAlbumListViewModel(groupAlbumUseCase, dataStoreUseCase)
        viewModel.groupAlbumModelList.value = testGroupAlbumModelList
    }

    @Test
    fun testSetIsCheckboxVisible() {
        runBlocking {
            viewModel.groupAlbumModelList.value!!.data[0].isCheckboxVisible = false
            viewModel.setIsCheckboxVisible(true)
            assertTrue(viewModel.groupAlbumModelList.value!!.data[0].isCheckboxVisible)
        }
    }
}