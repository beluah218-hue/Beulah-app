package com.example

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

object MobileOtpManager {
    var phoneNumber by mutableStateOf("+91 98765 43210")
    var isVerified by mutableStateOf(false)
    var generatedOtpCode by mutableStateOf("")
    var isOtpSent by mutableStateOf(false)
    var isMemberAccessGranted by mutableStateOf(false)
    var lastSmsNotification by mutableStateOf<String?>(null)

    fun sendOtp(phone: String): String {
        phoneNumber = phone.ifBlank { "+91 98765 43210" }
        val code = (100000 + Random.nextInt(900000)).toString()
        generatedOtpCode = code
        isOtpSent = true
        lastSmsNotification = "📩 SMS to $phoneNumber: Your Life Care security OTP is $code. Use this to verify single member data access."
        return code
    }

    fun verifyOtp(enteredCode: String): Boolean {
        if (enteredCode.trim() == generatedOtpCode.trim() && generatedOtpCode.isNotBlank()) {
            isVerified = true
            isMemberAccessGranted = true
            lastSmsNotification = null
            return true
        }
        return false
    }

    fun removeOtpAndReset() {
        isVerified = false
        isOtpSent = false
        generatedOtpCode = ""
        isMemberAccessGranted = false
        lastSmsNotification = null
    }
}
