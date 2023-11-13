package com.rasel.androidbaseapp.data.network

import com.rasel.androidbaseapp.BuildConfig
import com.rasel.androidbaseapp.data.network.responses.LoginResponse
import com.rasel.androidbaseapp.data.network.responses.UnsplashSearchResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MyApi {

    /*  @FormUrlEncoded
      @POST("api/login")
      suspend fun userLogin(
          @Field("login") emailOrPassword: String,
          @Field("password") password: String,
          @Field("fcm_token") fcm_token: String
      ): Response<LoginResponse>

      @Multipart
      @POST("api/reg")
      suspend fun userSignup(
          @Part part: MultipartBody.Part?,
          @PartMap params: HashMap<String, RequestBody>
      ): Response<SignUpResponse>

      @Multipart
      @POST("api/leaves")
      suspend fun submitLeaveApplication(
          @Header("Authorization") token: String,
          @Part part: MultipartBody.Part?,
          @PartMap map: HashMap<String, RequestBody>
      ): Response<LeaveApplySubmitResponse>

      @Multipart
      @PUT("api/leaves/update/{id}")
      suspend fun submitDraftLeaveApplication(
          @Header("Authorization") token: String,
          @Path("id") id: Int,
          @Part part: MultipartBody.Part?,
          @PartMap map: HashMap<String, RequestBody>
      ): Response<LeaveApplySubmitResponse>

      @GET("api/all_static_data")
      suspend fun userSignupData(
      ): Response<SignUpStaticDataResponse>

      @GET("api/leaves/create")
      suspend fun getLeaveTypeForUser(
          @Header("Authorization") token: String
      ): Response<LeaveTypeResponse>

      @GET("api/after_logged_user")
      suspend fun getLeaveApplyStaticData(
          @Header("Authorization") token: String
      ): Response<LeaveApplyStaticDataResponse>

      @GET("api/leaves")
      suspend fun getLeaveHistory(
          @Header("Authorization") token: String
      ): Response<LeaveHistoryResponse>

      @GET("api/leaveApprovals")
      suspend fun getLeaveRequest(
          @Header("Authorization") token: String
      ): Response<LeaveRequestResponse>

      @GET("api/address_info")
      suspend fun getAddressInfo(): Response<AddressInfoResponse>

      @GET("api/academic_calendar")
      suspend fun getAcademicCalendar(): Response<AcademicCalendarResponse>

      @GET("api/leaveApprovals/{id}")
      suspend fun getLeaveApproveSubmitStaticData(
          @Header("Authorization") token: String,
          @Path("id") id: Int
      ): Response<LeaveApproveSubmitStaticData>

      @FormUrlEncoded
      @POST("api/leaveApprovals")
      suspend fun submitLeaveApproval(
          @Header("Authorization") token: String,
          @FieldMap map: HashMap<String, String>
      ): Response<LeaveApprovalSubmitResponse>

      @GET("api/leave_heads")
      suspend fun getAllLiveType(
          @Header("Authorization") token: String
      ): Response<AllLeaveTypeResponse>

      @GET("api/all_users")
      suspend fun getAllUserInfo(
          @Header("Authorization") token: String
      ): Response<AllUserInfoResponse>

      @GET("api/dashboard")
      suspend fun getCurrentUserAvailableLeave(
          @Header("Authorization") token: String
      ): Response<CurrentUserLeaveResponse>

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

      @GET("https://www.bsmrmu.edu.bd/leave/api/leaves/change_info")
      suspend fun checkIfStationary(
          @Header("Authorization") token: String,
          @Query("from_date") startDate: String,
          @Query("to_date") endDate: String,
          @Query("medical_leave") medical_leave: String,
          @Query("user_id") userId: String,
          @Query("leave_head_id") leaveHeadId: String
      ): Response<CheckIfStationaryResponse>

      @Multipart
      @POST("api/stations")
      suspend fun submitStationLeaveApplication(
          @Header("Authorization") token: String,
          @PartMap map: HashMap<String, RequestBody>
      ): Response<StationLeaveApplicationResponse>

      @GET("api/stations/{id}")
      suspend fun getSLHistoryDetails(
          @Header("Authorization") token: String,
          @Path("id") id: Int
      ): Response<SLHDetailsResponse>


      @FormUrlEncoded
      @POST("api/stations/leaveApprovestore")
      suspend fun submitStationLeaveApproval(
          @Header("Authorization") token: String,
          @FieldMap map: HashMap<String, String>
      ): Response<SLRDetailsApproveSubmitResponse>


      @GET("api/getUnread")
      suspend fun getNotification(
          @Header("Authorization") token: String
      ): Response<NotificationResponse>

      @GET("api/leaves")
      suspend fun getMyLeaveHistoryRefreshedData(
          @Header("Authorization") token: String
      ): Response<LeaveHistoryResponse>



      @GET("api/leaveApprovals")
     suspend fun getLeaveRequest(
          @Header("Authorization") token: String,
          @Query("page") page: Int
      ): Response<LeaveRequestResponse>


      @GET("api/leaves")
      fun getLeaveHistory(
          @Header("Authorization") token: String,
          @Query("page") page: Int
      ): Call<LeaveHistoryResponse>*/


    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): UnsplashSearchResponse

    @GET("search/photos")
    suspend fun getDataFromUnSplash(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): Response<UnsplashSearchResponse>

    @GET("https://jsonplaceholder.typicode.com/posts")
    suspend fun getNotifications(
    ): NotificationResponse

    companion object {


        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {

            val okkHttpclient = OkHttpClient.Builder()
                .connectTimeout(20L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .writeTimeout(30L, TimeUnit.SECONDS)
                .addInterceptor(networkConnectionInterceptor)
                .build()

            val baseUrl = "https://api.unsplash.com/" // Live Server

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}

