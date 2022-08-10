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
import org.soma.everyonepick.groupalbum.domain.usecase.PhotoUseCase
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.PhotoListViewModel
import org.soma.everyonepick.groupalbum.util.testPhotoModel
import javax.inject.Inject

@HiltAndroidTest
class PhotoListViewModelTest {
    private lateinit var viewModel: PhotoListViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)

    @Inject lateinit var photoUseCase: PhotoUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = PhotoListViewModel(photoUseCase)
        viewModel.photoItemList.value = mutableListOf(testPhotoModel)
    }

    @Test
    fun testSetIsCheckboxVisible() {
        runBlocking {
            viewModel.photoItemList.value!![0].isCheckboxVisible = false
            viewModel.setIsCheckboxVisible(true)
            assertTrue(viewModel.photoItemList.value!![0].isCheckboxVisible)
        }
    }
}