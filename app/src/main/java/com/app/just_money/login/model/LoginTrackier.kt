package com.app.just_money.login.model

data class LoginTrackier(val success: Boolean?, val data: Data?)

data class Data(val user: User?)

data class User(val id: String?)
