package com.app.just_money.common_helper

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
}