package com.rasel.androidbaseapp.data.repositories

import com.rasel.androidbaseapp.data.db.AppDatabase
import com.rasel.androidbaseapp.data.db.entities.User
import com.rasel.androidbaseapp.data.network.MyApi
import com.rasel.androidbaseapp.data.network.SafeApiRequest


class UserRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

   /* suspend fun userLogin(email: String, password: String, fcmToken: String): LoginResponse {
        return apiRequest { api.userLogin(email, password, fcmToken) }
    }*/


    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getuser()

   /* suspend fun saveSignUpData(signUpStaticDataResponse: SignUpStaticDataResponse){

        signUpStaticDataResponse.data?.departments?.let {
            if(it.isNotEmpty()){
                db.getSignUpDataDao().saveAllDepartment(it)
            }else{
                Timber.d( "dep size : "+it.size)
            }
        }
        signUpStaticDataResponse.data?.designations?.let {
            if(it.isNotEmpty()){
                db.getSignUpDataDao().saveAllDesignation(it)
            }else{
                Timber.d( "desig size : "+it.size)
            }
            Timber.d( "desig size : "+db.getSignUpDataDao().getDepartments().value?.size)

        }
        signUpStaticDataResponse.data?.userType?.let {
            if(it.isNotEmpty()){
                db.getSignUpDataDao().saveUserType(it)
            }else{
                Timber.d( "desig size : "+it.size)
            }
            Timber.d( "desig size : "+db.getSignUpDataDao().getUserType().value?.size)

        }
    }

   suspend fun getAcademicCalendar(): AcademicCalendarResponse? {
       return if(db.getStaticDataDao().checkIfAcademicCalendarListIsEmpty().value == null){
           apiRequest { api.getAcademicCalendar() }
       }else null
    }

   suspend fun saveAcademicCalendar(list: List<DataAcademicCalendar>) {
        db.getStaticDataDao().clearAcademicCalendar()
        db.getStaticDataDao().saveAcademicCalendar(list)
    }

    fun getDepartment(): LiveData<List<Department>> = db.getSignUpDataDao().getDepartments()
    fun getDesignationList(): LiveData<List<Designation>> = db.getSignUpDataDao().getDesignation()
    fun getUserType(): LiveData<List<String>> = db.getSignUpDataDao().getUserType()

    suspend fun getSignUpData(): SignUpStaticDataResponse? {
        return if(db.getSignUpDataDao().checkIfDepartmentListIsEmpty().value == null){
            apiRequest {
                Timber.d( "fetching data from web for Sign Up Data")
                api.userSignupData()
            }
        }else null
    }

   suspend fun getDesignationById(designationId: Int) : Designation? {
       val temp = db.getSignUpDataDao().getDesignationById(designationId)

       return temp
    }

    fun getDepartmentById(id: Int): Department? {
        return db.getSignUpDataDao().getDepartmentById(id)
    }

    suspend fun getRequList (token : String, page : Int) : LeaveRequestResponse {
        return apiRequest { api.getLeaveRequest(token, 1) }
    }*/


}