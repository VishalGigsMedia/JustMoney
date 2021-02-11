package com.app.just_money.dagger

import com.app.just_money.available.model.AvailableOfferModel
import com.app.just_money.available.model.VersionModel
import com.app.just_money.in_progress.model.InProgressModel
import com.app.just_money.login.model.*
import com.app.just_money.my_wallet.completed.model.CompletedOfferModel
import com.app.just_money.my_wallet.faq.model.FaqModel
import com.app.just_money.my_wallet.setting.model.*
import com.app.just_money.offer_details.model.OfferDetailsModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface API {

    @POST("get_otp")
    fun getOTP(@Body requestKeyHelper: RequestKeyHelper): Call<GetOtpModel>

    @POST("login2")
    fun login(@Body requestKeyHelper: RequestKeyHelper): Call<LoginModel>

    @POST("signup")
    fun register(@Body requestKeyHelper: RequestKeyHelper): Call<RegisterUserModel>

    @POST("get_offers")
    fun getOffers(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<AvailableOfferModel>

    @POST("get_faq")
    fun getFaq(@Header("Authorization") authorizationToken: String): Call<FaqModel>

    @POST("get_offer_details")
    fun getOfferDetails(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<OfferDetailsModel>


    @POST("publishers")
    fun trackSignUp(@HeaderMap header: HashMap<String, String>, @HeaderMap header1: HashMap<String, String>, @Body requestKeyHelper: RequestKeyHelper): Call<SignUpTrackier>


    @POST("login")
    fun trackLogin(@HeaderMap header: HashMap<String, String>, @Body requestKeyHelper: RequestKeyHelper): Call<LoginTrackier>


    @POST("claim_offer")
    fun claimOffer(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<ClaimOfferModel>

    @POST("get_completed_offers")
    fun getCompletedOffers(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<CompletedOfferModel>

    @POST("get_pending_offers")
    fun getInProgressOffers(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<InProgressModel>

    @Multipart
    @POST("help_form")
    fun sendFeedback(@Header("Authorization") authKey: String?, @Part fileImage: MultipartBody.Part?, @Part("name") firstname: RequestBody?, @Part("email") mobile: RequestBody?, @Part("subject") lastname: RequestBody?, @Part("message") email: RequestBody?): Call<SendFeedbackModel>


    @GET("get_user_data")
    fun getUserProfileData(@Header("Authorization") authorizationToken: String): Call<GetUserProfileModel>

    @Multipart
    @POST("update_profile")
    fun updateProfile(@Header("Authorization") authKey: String?, @Part fileImage: MultipartBody.Part?, @Part("first_name") firstName: RequestBody?, @Part("last_name") lastName: RequestBody?, @Part("gender") gender: RequestBody?, @Part("dob") dob: RequestBody?, @Part("email") email: RequestBody?): Call<UpdateProfileModel>

    @GET("logout")
    fun logout(@Header("Authorization") authorizationToken: String): Call<LogoutModel>


    @GET("send_email_verification_otp")
    fun sendEmailVerificationOtp(@Header("Authorization") authorizationToken: String): Call<SendEmailOtpModel>


    @POST("verify_email_otp")
    fun verifyEmailOtp(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<VerifyEmailOtpModel>

    @POST("check_version")
    fun checkVersion(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<VersionModel>

}