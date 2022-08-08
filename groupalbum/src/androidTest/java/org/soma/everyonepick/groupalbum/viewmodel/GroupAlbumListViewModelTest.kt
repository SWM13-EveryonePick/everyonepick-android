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
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository
import org.soma.everyonepick.groupalbum.util.testGroupAlbumItem
import org.soma.everyonepick.groupalbum.util.testGroupAlbumItemList
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

    @Inject lateinit var groupAlbumRepository: GroupAlbumRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = GroupAlbumListViewModel(groupAlbumRepository)
        viewModel.groupAlbumItemList.value = testGroupAlbumItemList
    }

    @Test
    fun testSetIsCheckboxVisible() {
        runBlocking {
            viewModel.groupAlbumItemList.value!!.data[0].isCheckboxVisible = false
            viewModel.setIsCheckboxVisible(true)
            assertTrue(viewModel.groupAlbumItemList.value!!.data[0].isCheckboxVisible)
        }
    }
}