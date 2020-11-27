package com.app.just_money.dagger

import com.app.just_money.login.model.GetOtpModel
import com.app.just_money.login.model.LoginModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface API {

    @POST("get_otp")
    fun getOTP(@Body requestKeyHelper: RequestKeyHelper): Call<GetOtpModel>

    @POST("login")
    fun login(@Body requestKeyHelper: RequestKeyHelper): Call<LoginModel>

    @POST("login")
    fun getOffers(
        @Body requestKeyHelper: RequestKeyHelper
    ): Call<LoginModel>

}