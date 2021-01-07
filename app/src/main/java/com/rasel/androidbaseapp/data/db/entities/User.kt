package com.rasel.androidbaseapp.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings

const val CURRENT_USER_ID = 0

@Entity
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
data class User(

    val id: Int?,
    val designation_id: Int?,
    val national_id: String?,
    val employeeId: String?,
    val blood_group: String?,
    val image: String?,
    val present_address: String?,
    val permanent_address: String?,
    val joining_date: String?,
    val dob: String?,
    val type: String?,
    val department_id: Int?,
    val name: String?,
    val email: String?,
    val username: String?,
    val p_no: String?,
    val email_verified_at: String?,
    val sex: String?,
    val about: String?,
    val status: String?,
    val created_at: String?,
    val updated_at: String?,
    val identification_mark: String?,
    val full: String?,
    val nameEmail: String?,
    val FullUrl: String?,

    @Embedded(prefix = "desig_")
    val designation: Designation?,

    @Embedded(prefix = "dept_")
    val department: Department?


) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}