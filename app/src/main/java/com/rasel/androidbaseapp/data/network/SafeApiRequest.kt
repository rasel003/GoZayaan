package com.rasel.androidbaseapp.data.network

import com.orhanobut.logger.Logger
import com.rasel.androidbaseapp.util.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        val response = call.invoke()

       Logger.d("response raw : ${response.raw()}")

        if(response.isSuccessful){
            return response.body()!!
        }else{
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let{
                try{
                    message.append(JSONObject(it).getString("error"))
                }catch(e: JSONException){ }
              //  message.append("\n")
            }
            //message.append("Error Code: ${response.code()}")
            throw ApiException(message.toString())
        }
    }

}