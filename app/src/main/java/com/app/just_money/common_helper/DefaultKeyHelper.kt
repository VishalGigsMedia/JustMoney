package com.app.just_money.common_helper

import com.app.just_money.BuildConfig

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

    //trackier
    const val TR_DEV_KEY = "7a6fc85c-f35a-42d8-8612-20e8bd950962"

    //fragments
    const val availableFragment = "AVAILABLE_FRAGMENT"
    const val inProgressFragment = "IN_PROGRESS_FRAGMENT"
    const val walletFragment = "WALLET_FRAGMENT"

    //social platforms package id
    const val FACEBOOK = "com.facebook.katana"
    const val TWITTER = "com.twitter.android"
    const val GMAIL = "com.google.android.gm"
    const val WHATSAPP = "com.whatsapp"

    //leader board types
    var weekly = DefaultHelper.encrypt("weekly")
    var monthly = DefaultHelper.encrypt("monthly")
}