package com.app.cent4free.common_helper

object DefaultValidationHelper {

    fun isValidMobileNumber(mobile: String): Boolean {
        if (mobile.isEmpty()) {
            return false
        }
        return true
    }

    fun isValidString(value: String): Boolean {
        if (value.isEmpty() || value.length < 0) {
            return false
        }
        return true
    }

    fun checkPwdMatching(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            return false
        }
        return true
    }


}