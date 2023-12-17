package com.rasel.androidbaseapp.data.repositories

import android.content.Context
import com.rasel.androidbaseapp.data.db.dao.PlantDao
import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.network.Resource
import com.rasel.androidbaseapp.data.network.responses.PostItem
import com.rasel.androidbaseapp.data.preferences.PreferenceProvider
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class HomeRepositoryTest {
    @Mock
    lateinit var myApi: MyApi

    @Mock
    lateinit var plantDao: PlantDao

    lateinit var preferenceProvider: PreferenceProvider

    protected lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetProducts_EmptyList() = runTest {
        Mockito.`when`(myApi.getPostList()).thenReturn(emptyList())

        val sut = HomeRepository(myApi, plantDao, preferenceProvider)
        val result = sut.getPostList()
        Assert.assertEquals(true, result is Resource.Success)
        Assert.assertEquals(0, (result as Resource.Success).value.size)
    }

    @Test
    fun testGetProducts_expectedProductList() = runTest {
        val productList = listOf(
            PostItem(1, "Prod 1", anyString(), anyInt()),
            PostItem(2, anyString(), anyString(), anyInt())
        )
        Mockito.`when`(myApi.getPostList()).thenReturn(productList)

        val sut = HomeRepository(myApi, plantDao, preferenceProvider)
        val result = sut.getPostList()
        Assert.assertEquals(true, result is Resource.Success)
        Assert.assertEquals(2, (result as Resource.Success).value.size)
        Assert.assertEquals("Prod 1", result.value[0].title)
    }

  /*  @Test
    fun testGetProducts_expectedError() = runTest {
        Mockito.`when`(myApi.getPostList())
            .thenReturn(Response.error(401, "Unauthorized".toResponseBody()))

        val sut = LocalizationRepository(myApi)
        val result = sut.getProducts()
        Assert.assertEquals(true, result is NetworkResult.Error)
        Assert.assertEquals("Something went wrong", result.message)
    }*/
}