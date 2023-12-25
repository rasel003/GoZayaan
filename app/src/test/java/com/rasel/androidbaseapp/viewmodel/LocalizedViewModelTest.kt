package com.rasel.androidbaseapp.viewmodel

import com.rasel.androidbaseapp.data.repositories.LocalizationRepository
import com.rasel.androidbaseapp.utils.PresentationBaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class LocalizedViewModelTest : PresentationBaseTest(){

    @Mock
    lateinit var repository: LocalizationRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher = dispatcher.main)
    }

   /* @Test
    fun test_GetProducts() = runTest{
        Mockito.`when`(repository.getProducts()).doReturn(NetworkResult.Success(emptyList()))

        val sut = LocalizedViewModel(repository)
        sut.getProducts()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.products.getOrAwaitValue()
        assertEquals(0, result.data!!.size)
    }

    @Test
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