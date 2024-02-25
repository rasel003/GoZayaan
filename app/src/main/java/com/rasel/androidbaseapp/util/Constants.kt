package com.rasel.androidbaseapp.util

import com.google.gson.Gson
import com.rasel.androidbaseapp.data.models.signin.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

/**
 * Constants used throughout the app.
 */

const val DATABASE_NAME = "sunflower-db"
const val PLANT_DATA_FILENAME = "plants.json"

const val KEY_TOKEN_PREFERENCE = "key_token"
const val KEY_TOKEN_ROLE_ID = "role_id"
const val KEY_FCM_TOKEN_PREFERENCE = "key_fcm_token"
const val KEY_LAST_SAVED_PREFERENCE = "key_fcm_token"
const val KEY_DASHBOARD_LAST_SAVED = "key_dashboard_last_saved"
const val KEY_SEX = "key_sex"


const val CHANNEL_ID_RECEIVED_LEAVE_REQUEST = "ID_RECEIVED_LEAVE_REQUEST"
const val MINIMUM_INTERVAL_IN_MINUTE_ORDER = 60


enum class AppLanguage(val value: String) {
    ENGLISH("English"),
    BURMESE("Burmese"),
    CHINESE("Chinese");
}


fun<T> apiRequestFlow(call: suspend () -> Response<T>): Flow<ApiResponse<T>> = flow {
    emit(ApiResponse.Loading)

    withTimeoutOrNull(20000L) {
        val response = call()

        try {
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(ApiResponse.Success(data))
                }
            } else {
                response.errorBody()?.let { error ->
                    error.close()
                    val parsedError: ErrorResponse = Gson().fromJson(error.charStream(), ErrorResponse::class.java)
                    emit(ApiResponse.Failure(parsedError.message, parsedError.code))
                }
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: e.toString(), 400))
        }
    } ?: emit(ApiResponse.Failure("Timeout! Please try again.", 408))
}.flowOn(Dispatchers.IO)