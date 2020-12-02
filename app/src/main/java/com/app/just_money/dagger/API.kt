package com.app.just_money.dagger

import com.app.just_money.available.model.AvailableOfferModel
import com.app.just_money.login.model.*
import com.app.just_money.my_wallet.completed.model.CompletedOfferModel
import com.app.just_money.my_wallet.faq.model.FaqModel
import com.app.just_money.offer_details.model.OfferDetailsModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import java.util.*

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

    @POST("get_offer_details")
    fun getOfferDetails(
        @Header("Authorization") authorizationToken: String,
        @Body requestKeyHelper: RequestKeyHelper
    ): Call<OfferDetailsModel>


    @POST("publishers")
    fun trackSignUp(
        @HeaderMap header: HashMap<String, String>,
        @HeaderMap header1: HashMap<String, String>,
        @Body requestKeyHelper: RequestKeyHelper
    ): Call<SignUpTrackier>


    @POST("login")
    fun trackLogin(
        @HeaderMap header: HashMap<String, String>,
        @Body requestKeyHelper: RequestKeyHelper
    ): Call<LoginTrackier>


    @POST("claim_offer")
    fun claimOffer(
        @Header("Authorization") authorizationToken: String,
        @Body requestKeyHelper: RequestKeyHelper
    ): Call<ClaimOfferModel>

    @POST("get_completed_offers")
    fun getCompletedOffers(
        @Header("Authorization") authorizationToken: String,
        @Body requestKeyHelper: RequestKeyHelper
    ): Call<CompletedOfferModel>

    @POST("get_pending_offers")
    fun getInProgressOffers(
        @Header("Authorization") authorizationToken: String,
        @Body requestKeyHelper: RequestKeyHelper
    ): Call<CompletedOfferModel>

}