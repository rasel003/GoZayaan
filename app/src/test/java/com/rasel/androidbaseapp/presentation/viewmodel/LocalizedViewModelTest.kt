package com.rasel.androidbaseapp.presentation.viewmodel

import androidx.lifecycle.asLiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.rasel.androidbaseapp.data.LocalizationRepositoryImp
import com.rasel.androidbaseapp.fakes.FakeData
import com.rasel.androidbaseapp.getOrAwaitValue
import com.rasel.androidbaseapp.util.AppLanguage
import com.rasel.androidbaseapp.utils.PresentationBaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalizedViewModelTest : PresentationBaseTest() {

    @Mock
    lateinit var repository: LocalizationRepositoryImp

    private lateinit var sut: LocalizedViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher = dispatcher.main)
        sut = LocalizedViewModel(repository)
    }

    @Test
    fun `switch to english should return english localization`() = runTest {
        val localization = FakeData.getLocalization(AppLanguage.ENGLISH)
        Mockito.`when`(repository.localizationFlow).doReturn(MutableStateFlow(localization))

        val sut = LocalizedViewModel(repository)
        sut.switchToEnglish()
        val result = sut.localizationFlow.asLiveData().getOrAwaitValue()
        assertEquals("English", result.lblEnglish)
    }

    @Test
    fun `switch to burmese should return burmese localization`() = runTest {
        val localization = FakeData.getLocalization(AppLanguage.BURMESE)
        Mockito.`when`(repository.localizationFlow).doReturn(MutableStateFlow(localization))

        val sut = LocalizedViewModel(repository)
        sut.switchToBurmese()
        val result = sut.localizationFlow.asLiveData().getOrAwaitValue()
        assertEquals("အင်္ဂလိပ်", result.lblEnglish)
    }

    @Test
    fun `switch to chinese should return chinese localization`() = runTest {
        val localization = FakeData.getLocalization(AppLanguage.CHINESE)
        Mockito.`when`(repository.localizationFlow).doReturn(MutableStateFlow(localization))

        val sut = LocalizedViewModel(repository)
        sut.switchToBurmese()
        val result = sut.localizationFlow.asLiveData().getOrAwaitValue()
        assertEquals("英语", result.lblEnglish)
    }

    /* @Test
     fun test_GetProduct_expectedError() = runTest{
         Mockito.`when`(repository.getProducts()).thenReturn(NetworkResult.Error("Something Went Wrong"))

         val sut = LocalizedViewModel(repository)
         sut.getProducts()
         testDispatcher.scheduler.advanceUntilIdle()
         val result = sut.products.getOrAwaitValue()
         assertEquals(true, result is NetworkResult.Error)
         assertEquals("Something Went Wrong", result.message)
     }*/


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}