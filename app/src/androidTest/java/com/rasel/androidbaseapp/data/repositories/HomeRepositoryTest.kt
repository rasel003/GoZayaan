package com.rasel.androidbaseapp.data.repositories

import android.content.Context
import androidx.test.filters.MediumTest
import com.rasel.androidbaseapp.cache.dao.PlantDao
import com.rasel.androidbaseapp.remote.api.MyApi
import com.rasel.androidbaseapp.remote.utils.Resource
import com.rasel.androidbaseapp.remote.models.PostItem
import com.rasel.androidbaseapp.cache.preferences.PreferenceProvider
import com.rasel.androidbaseapp.data.HomeRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class HomeRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var myApi: MyApi

    @Inject
    lateinit var plantDao: PlantDao

    @Inject
    lateinit var preferenceProvider: PreferenceProvider

    protected lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
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