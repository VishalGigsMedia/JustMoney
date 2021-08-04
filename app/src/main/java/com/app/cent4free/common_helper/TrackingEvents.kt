package com.app.cent4free.common_helper

import com.app.cent4free.dagger.MyApplication
import com.trackier.sdk.TrackierEvent
import com.trackier.sdk.TrackierSDK

object TrackingEvents {
    val preferenceHelper = PreferenceHelper(MyApplication.instance)
    private const val offerListEventId = "_PISHbyztZ"
    private const val dailyRewardId = "tUxuAdyMQy"
    private const val offerEarnId = "ibBnCDfH5W"
    //private const val requestPayoutId = "-iw-K1lEgD"
    private const val profileEditedId = "wVQRV6rLn9"
    private const val referredId = "g8XJs7tQQ1"
    private const val sharedTheAppId = "moG2jMeuK9"
    private const val likeFbButtonId = "ISJPxufDli"
    private const val joinTelegramButtonId = "P4Cc5yPXi1"
    private const val leaderBoardId = "F9_fes2mPA"

    fun trackOfferList() {
        val event = TrackierEvent(offerListEventId)
        event.param1 = preferenceHelper.getUserId()
        TrackierSDK.trackEvent(event)
    }
    fun trackDailyReward(amount: String) {
        val event = TrackierEvent(dailyRewardId)
        event.param1 = preferenceHelper.getUserId()
        event.param2 = amount
        TrackierSDK.trackEvent(event)
    }
    fun trackOfferEarned(offerId: String) {
        val event = TrackierEvent(offerEarnId)
        event.param1 = preferenceHelper.getUserId()
        event.param2 = offerId//offerId
        TrackierSDK.trackEvent(event)
    }
    /*fun trackRequestedPayout(amount: String) {
        val event = TrackierEvent(requestPayoutId)
        event.param1 = preferenceHelper.getUserId()
        event.param2 = amount
        TrackierSDK.trackEvent(event)
    }*/
    fun trackProfileEdited() {
        val event = TrackierEvent(profileEditedId)
        event.param1 = preferenceHelper.getUserId()
        TrackierSDK.trackEvent(event)
    }
    fun trackReferred(sharedOn :String) {
        val event = TrackierEvent(referredId)
        event.param1 = preferenceHelper.getUserId()
        event.param2 = sharedOn
        TrackierSDK.trackEvent(event)
    }
    fun trackAppShared() {
        val event = TrackierEvent(sharedTheAppId)
        event.param1 = preferenceHelper.getUserId()
        event.param2 = "Shared from Settings"
        TrackierSDK.trackEvent(event)
    }
    fun trackFBLikeClicked(page:String) {
        val event = TrackierEvent(likeFbButtonId)
        event.param1 = preferenceHelper.getUserId()
        event.param2 = page
        TrackierSDK.trackEvent(event)
    }
    fun trackJoinTelegramClicked(page:String) {
        val event = TrackierEvent(joinTelegramButtonId)
        event.param1 = preferenceHelper.getUserId()
        event.param2 = page
        TrackierSDK.trackEvent(event)
    }
    fun trackLeaderBoardViewed() {
        val event = TrackierEvent(leaderBoardId)
        event.param1 = preferenceHelper.getUserId()
        TrackierSDK.trackEvent(event)
    }

}