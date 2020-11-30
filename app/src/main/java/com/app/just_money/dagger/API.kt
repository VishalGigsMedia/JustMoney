package com.app.just_money.dagger

import com.app.just_money.available.model.AvailableOfferModel
import com.app.just_money.login.model.GetOtpModel
import com.app.just_money.login.model.LoginModel
import com.app.just_money.my_wallet.faq.model.FaqModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface API {

    @POST("get_otp")
    fun getOTP(@Body requestKeyHelper: RequestKeyHelper): Call<GetOtpModel>

    @POST("login")
    fun login(@Body requestKeyHelper: RequestKeyHelper): Call<LoginModel>

    @POST("get_offers")
    fun getOffers(
        @Header("Authorization") authorizationToken: String,
        @Body requestKeyHelper: RequestKeyHelper
    ): Call<AvailableOfferModel>

    @POST("get_faq")
    fun getFaq(
        @Header("Authorization") authorizationToken: String
    ): Call<FaqModel>
}