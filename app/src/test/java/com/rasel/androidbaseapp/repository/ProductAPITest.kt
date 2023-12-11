package com.rasel.androidbaseapp.repository

import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.repositories.ProductRepository
import com.rasel.androidbaseapp.util.NetworkResult
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ProductAPITest {

    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: MyApi


    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MyApi::class.java)
    }

    @Test
    fun testGetProducts_EmptyList() = runTest {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(
            mockResponse
                .setResponseCode(404)
        )
        val sut = ProductRepository(apiService)
        val result = sut.getProducts()
        val request = mockWebServer.takeRequest()

        Assert.assertEquals(true, result is NetworkResult.Success)
        Assert.assertEquals(0, result.data!!.size)
    }

}