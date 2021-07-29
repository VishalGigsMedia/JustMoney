package com.app.just_money.dagger

import com.app.just_money.available.model.AvailableOfferModel
import com.app.just_money.available.model.IpApiModel
import com.app.just_money.in_progress.model.InProgressModel
import com.app.just_money.login.model.*
import com.app.just_money.login.model.login.LoginModel
import com.app.just_money.model.CheckAppVersionModel
import com.app.just_money.my_wallet.completed.model.CompletedOfferModel
import com.app.just_money.my_wallet.faq.model.FaqModel
import com.app.just_money.my_wallet.leaderborard.model.LeaderBoardModel
import com.app.just_money.my_wallet.model.EarningsModel
import com.app.just_money.my_wallet.payouts.model.PayoutModel
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

    @POST("login")
    fun login(@Body requestKeyHelper: RequestKeyHelper): Call<LoginModel>

    @POST("forget-password")
    fun forgotPassword(@Body requestKeyHelper: RequestKeyHelper): Call<ForgotPasswordModel>

    @POST("signup")
    fun register(@Body requestKeyHelper: RequestKeyHelper): Call<RegisterUserModel>

    @POST("get-offers")
    fun getOffers(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<AvailableOfferModel>

    @GET("faqs")
    fun getFaq(@Header("Authorization") authorizationToken: String): Call<FaqModel>

    @GET("terms-and-conditions")
    fun getTC(@Header("Authorization") authorizationToken: String): Call<FaqModel>

    @GET("privacy-policy")
    fun getPP(@Header("Authorization") authorizationToken: String): Call<FaqModel>

    @POST("get-offer-details")
    fun getOfferDetails(@Header("Authorization") authorizationToken: String,
        @Body requestKeyHelper: RequestKeyHelper): Call<OfferDetailsModel>


    @POST("publishers")
    fun trackSignUp(@HeaderMap header: HashMap<String, String>, @HeaderMap header1: HashMap<String, String>,
        @Body requestKeyHelper: RequestKeyHelper): Call<SignUpTrackier>


    @POST("login")
    fun trackLogin(@HeaderMap header: HashMap<String, String>, @Body requestKeyHelper: RequestKeyHelper): Call<LoginTrackier>


    @POST("claim-offer")
    fun claimOffer(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<ClaimOfferModel>

    @POST("get-daily-reward")
    fun claimReward(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<ClaimOfferModel>

    @POST("earnings")
    fun getEarnings(@Header("Authorization") authorizationToken: String): Call<EarningsModel>

    @POST("request-payout")
    fun requestPayout(@Header("Authorization") authorizationToken: String,
        @Body requestKeyHelper: RequestKeyHelper): Call<EarningsModel>

    @POST("get-completed-offers")
    fun getCompletedOffers(@Header("Authorization") authorizationToken: String,
        @Body requestKeyHelper: RequestKeyHelper): Call<CompletedOfferModel>

    @POST("get-pending-offers")
    fun getInProgressOffers(@Header("Authorization") authorizationToken: String): Call<InProgressModel>

    @Multipart
    @POST("help_form")
    fun sendFeedback(@Header("Authorization") authKey: String?, @Part fileImage: MultipartBody.Part?,
        @Part("name") firstname: RequestBody?, @Part("email") mobile: RequestBody?,
        @Part("subject") lastname: RequestBody?, @Part("message") email: RequestBody?): Call<SendFeedbackModel>


    @GET("get_user_data")
    fun getUserProfileData(@Header("Authorization") authorizationToken: String): Call<GetUserProfileModel>

    @Multipart
    @POST("update-user-profile")
    fun updateProfile(@Header("Authorization") authKey: String?, @Part fileImage: MultipartBody.Part?,
        @Part("first_name") firstName: RequestBody?, @Part("last_name") lastName: RequestBody?,
        @Part("gender") gender: RequestBody?, @Part("dob") dob: RequestBody?, @Part("email") email: RequestBody?,
        @Part("mobile") mobile: RequestBody?): Call<UpdatedProfileModel>

    @POST("logout")
    fun logout(@Header("Authorization") authorizationToken: String): Call<LogoutModel>

    @POST("verify_email_otp")
    fun verifyEmailOtp(@Header("Authorization") authorizationToken: String,
        @Body requestKeyHelper: RequestKeyHelper): Call<VerifyEmailOtpModel>

    @POST("appCurrentVersion") //check_version
    fun checkVersion(@Header("Authorization") authorizationToken: String): Call<CheckAppVersionModel>
    //fun checkVersion(@Header("Authorization") authorizationToken: String, @Body requestKeyHelper: RequestKeyHelper): Call<CheckAppVersionModel>

    @POST("payout-history")
    fun getPayoutHistory(@Header("Authorization") authorizationToken: String,
        @Body requestKeyHelper: RequestKeyHelper): Call<PayoutModel>

    @GET//getIPAddress
    fun getIPAddress(@Url url: String): Call<IpApiModel>

    @POST("leaderboard")//leader board
    fun getLeaderBoard(@Header("Authorization") authorizationToken: String): Call<LeaderBoardModel>
}