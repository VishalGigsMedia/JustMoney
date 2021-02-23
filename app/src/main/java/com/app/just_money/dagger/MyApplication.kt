package com.app.just_money.dagger

import android.app.Application
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultKeyHelper.TR_DEV_KEY
import com.trackier.sdk.TrackierSDK
import com.trackier.sdk.TrackierSDKConfig

class MyApplication : Application() {


    init {
        instance = this
    }

    companion object {
        lateinit var instance: MyApplication
        private val mInstance: MyApplication?
            @Synchronized get() {
                var appController: MyApplication?
                synchronized(MyApplication::class.java) {
                    appController = mInstance
                }
                return appController
            }
    }

    private var mApiComponent: ApiComponent? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        mApiComponent = DaggerApiComponent.builder().appModule(AppModule(this))
            .apiModule(ApiModule(getString(R.string.server_url))).build()
        mApiComponent?.inject(this)

        //trackier
        val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "development")
        TrackierSDK.initialize(sdkConfig)

    }

    fun getNetComponent(): ApiComponent? {
        return mApiComponent
    }
}