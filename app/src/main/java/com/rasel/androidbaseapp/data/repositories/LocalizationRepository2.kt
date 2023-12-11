package com.rasel.androidbaseapp.data.repositories

import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.network.responses.ProductListItem
import com.rasel.androidbaseapp.util.NetworkResult
import javax.inject.Inject

open class LocalizationRepository2  @Inject constructor(
    private val myApi: MyApi,
) {
    suspend fun getProducts(): NetworkResult<List<ProductListItem>> {
        val response = myApi.getProducts()
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkResult.Success(responseBody)
            } else {
                NetworkResult.Error("Something went wrong")
            }
        } else {
            NetworkResult.Error("Something went wrong")
        }
    }
}