package com.rasel.androidbaseapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.network.responses.ProductListItem
import com.rasel.androidbaseapp.data.repositories.LocalizationRepository2
import com.rasel.androidbaseapp.getOrAwaitValue
import com.rasel.androidbaseapp.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class LocalizedViewModelTest2 {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var myApi: MyApi

    @Mock
    lateinit var repository2: LocalizationRepository2

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun test_GetProducts() = runTest {
//        Mockito.`when`(repository2.getProducts()).doReturn(NetworkResult.Success(emptyList()))
        Mockito.doReturn(NetworkResult.Success(emptyList<ProductListItem>()))
            .`when`(repository2.getProducts())

        val sut = LocalizedViewModel2(repository2)
        sut.getProducts()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.products.getOrAwaitValue()
        assertEquals(0, result.data!!.size)
    }

    /* @Test
     fun test_GetProduct_expectedError() = runTest {
         Mockito.`when`(repository.getProducts())
             .thenReturn(NetworkResult.Error("Something Went Wrong"))

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