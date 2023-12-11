package com.rasel.androidbaseapp.data.repositories

import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.network.responses.ProductListItem
import com.rasel.androidbaseapp.util.NetworkResult

class ProductRepository(private val productsAPI: MyApi) {

    suspend fun getProducts(): NetworkResult<List<ProductListItem>> {
        val response = productsAPI.getProducts()
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