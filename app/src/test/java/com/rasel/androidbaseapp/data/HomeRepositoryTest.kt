package com.rasel.androidbaseapp.data

import com.rasel.androidbaseapp.data.repository.HomeDataSource
import com.rasel.androidbaseapp.data.source.HomeDataSourceFactory
import com.rasel.androidbaseapp.fakes.FakeValueFactory
import com.rasel.androidbaseapp.remote.models.PostItem
import com.rasel.androidbaseapp.remote.utils.Resource
import com.rasel.androidbaseapp.util.NetworkResult
import com.rasel.androidbaseapp.utils.DataBaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeRepositoryTest : DataBaseTest() {

    @Mock
    lateinit var dataSourceFactory: HomeDataSourceFactory

    @Mock
    lateinit var dataSource: HomeDataSource

    lateinit var sut: HomeRepository

    @Before
    fun setUp() {
        sut = HomeRepository(dataSourceFactory)
    }

    @Test
    fun testGetProducts_EmptyList() = runTest {
        Mockito.`when`(dataSourceFactory.getRemoteDataSource()).thenReturn(dataSource)
        Mockito.`when`(dataSource.getPostList()).thenReturn(Resource.Success(emptyList()))

        val result = sut.getPostList()
        Assert.assertEquals(true, result is Resource.Success)
        Assert.assertEquals(0, (result as Resource.Success).value.size)
    }

    @Test
    fun testGetProducts_expectedProductList() = runTest {
        val productList = listOf(
            PostItem(1, "Prod 1", FakeValueFactory.randomString(), FakeValueFactory.randomInt()),
            PostItem(
                2,
                FakeValueFactory.randomString(),
                FakeValueFactory.randomString(),
                FakeValueFactory.randomInt()
            )
        )
        Mockito.`when`(dataSourceFactory.getRemoteDataSource()).thenReturn(dataSource)
        Mockito.`when`(dataSource.getPostList()).thenReturn(Resource.Success(productList))

        val result = sut.getPostList()
        Assert.assertEquals(true, result is Resource.Success)
        Assert.assertEquals(2, (result as Resource.Success).value.size)
        Assert.assertEquals("Prod 1", result.value[0].title)
    }

      @Test
      fun testGetProducts_expectedError() = runTest {
          Mockito.`when`(dataSource.getPostList())
              .thenReturn(Response.error(401, "Unauthorized".toResponseBody()))

          val result = sut.getProducts()
          Assert.assertEquals(true, result is NetworkResult.Error)
          Assert.assertEquals("Something went wrong", result.message)
      }
}