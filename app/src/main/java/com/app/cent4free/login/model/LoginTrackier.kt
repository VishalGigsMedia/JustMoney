package com.app.cent4free.login.model

data class LoginTrackier(val success: Boolean?, val data: Data?)

data class Data(val user: User?)

data class User(val id: String?)
