package com.example

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

object EmailVerificationManager {
    var emailAddress by mutableStateOf("")
    var isCodeSent by mutableStateOf(false)
    var generatedCode by mutableStateOf("")
    var isEmailVerified by mutableStateOf(false)

    fun sendVerificationCode(email: String): String {
        emailAddress = email.trim()
        val code = (100000 + Random.nextInt(900000)).toString()
        generatedCode = code
        isCodeSent = true
        isEmailVerified = false
        return code
    }

    fun verifyCode(enteredCode: String): Boolean {
        if (enteredCode.trim() == generatedCode.trim() && generatedCode.isNotBlank()) {
            isEmailVerified = true
            return true
        }
        return false
    }

    fun reset() {
        emailAddress = ""
        isCodeSent = false
        generatedCode = ""
        isEmailVerified = false
    }
}
