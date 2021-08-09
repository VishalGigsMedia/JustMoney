package com.app.cent4free.common_helper

import com.app.cent4free.BuildConfig

object DefaultKeyHelper {

    const val preferenceName = "JustMoney"

    //play store link
    const val playStoreLink = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"

    //used for encryption and decryption
    const val secretKey = "KEY61E0671477E96"
    const val initializeVectorKey = "!gIm*fF3pUe3Kbm%"

    //server response code
    const val successCode: Int = 1
    const val failureCode: Int = 0
    const val forceLogoutCode: Int = 100

    const val xApiKey = "5e020255c70c3bde6bcf1f1c1275e020255c7151"
    const val GIGS_NATIVE_URL = "gigs.vnative.com"
    const val facebookPageId = "104354911699587"
    const val facebookPageUrl = "https://www.facebook.com/JustMoney-104354911699587"
    const val telegramUrl = "https://t.me/downloadnearn"

    const val male = "1"
    const val female = "2"

    //fragments
    const val availableFragment = "AVAILABLE_FRAGMENT"
    const val inProgressFragment = "IN_PROGRESS_FRAGMENT"
    const val walletFragment = "WALLET_FRAGMENT"
    const val profileFragment = "PROFILE_FRAGMENT"
    const val referAndEarn = "REFER_AND_EARN"

    //social platforms package id
    const val FACEBOOK = "com.facebook.katana"
    const val TWITTER = "com.twitter.android"
    const val GMAIL = "com.google.android.gm"
    const val WHATSAPP = "com.whatsapp"

    //leader board types
    var weekly = DefaultHelper.encrypt("weekly")
    var monthly = DefaultHelper.encrypt("monthly")

    //google
    const val webClientID = "403778887684-jk9cb36pkmignjffoduk2i33dl586hc4.apps.googleusercontent.com"

    //login type
    const val APP_LOGIN = "0"
    const val GOOGLE_LOGIN = "1"
}