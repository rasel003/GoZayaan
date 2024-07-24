package com.rasel.androidbaseapp.remote.api

import com.rasel.androidbaseapp.BuildConfig
import com.rasel.androidbaseapp.remote.models.PostItem
import com.rasel.androidbaseapp.remote.models.UnsplashSearchResponse
import retrofit2.Response
import retrofit2.http.*

interface MyApi {

    /*  @FormUrlEncoded
      @POST("api/login")
      suspend fun userLogin(
          @Field("login") emailOrPassword: String
      ): Response<LoginResponse>

      @Multipart
      @PUT("api/leaves/update/{id}")
      suspend fun submitDraftLeaveApplication(
          @Header("Authorization") token: String,
          @Path("id") id: Int,
          @Part part: MultipartBody.Part?,
          @PartMap map: HashMap<String, RequestBody>
      ): Response<LeaveApplySubmitResponse>


      @FormUrlEncoded
      @POST("api/leaveApprovals")
      suspend fun submitLeaveApproval(
          @Header("Authorization") token: String,
          @FieldMap map: HashMap<String, String>
      ): Response<LeaveApprovalSubmitResponse>

      @GET("api/messages/{id}")
      suspend fun getMailDetails(
          @Header("Authorization") token: String,
          @Path("id") id: Int
      ): Response<MailDetailsResponse>

      @Multipart
      @POST("api/messages")
      suspend fun sendMailToUser(
          @Header("Authorization") token: String,
          @Part parts: List<MultipartBody.Part>,
          @Query("select3[]") ccUserList: List<String>,
          @PartMap map: HashMap<String, RequestBody>
      ): Response<WriteMailResponse>


      @FormUrlEncoded
      @POST("api/stations/leaveApprovestore")
      suspend fun submitStationLeaveApproval(
          @Header("Authorization") token: String,
          @FieldMap map: HashMap<String, String>
      ): Response<SLRDetailsApproveSubmitResponse>


      @GET("api/leaves")
      fun getLeaveHistory(
          @Header("Authorization") token: String,
          @Query("page") page: Int
      ): Call<LeaveHistoryResponse>*/


    @GET("https://api.unsplash.com/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): UnsplashSearchResponse

    @GET("https://api.unsplash.com/search/photos")
    suspend fun getDataFromUnSplash(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): Response<UnsplashSearchResponse>
  @GET("https://api.unsplash.com/search/photos")
    suspend fun getDataFromUnSplash2(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): UnsplashSearchResponse

    @GET("https://jsonplaceholder.typicode.com/posts")
    suspend fun getPostList(): Response<List<PostItem>>

}

