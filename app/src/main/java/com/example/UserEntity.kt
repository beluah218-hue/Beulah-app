package com.example

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val age: String = "",
    val gender: String = "",
    val bloodGroup: String = "",
    val allergies: String = "",
    val primaryDoctorName: String = "",
    val primaryDoctorPhone: String = "",
    val emergencyContactName: String = "",
    val emergencyContactPhone: String = "",
    val isLoggedIn: Boolean = false,
    val isEmailVerified: Boolean = false,
    val loginTimestamp: Long = System.currentTimeMillis()
)
