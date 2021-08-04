package com.app.cent4free.dagger

import androidx.multidex.MultiDexApplication
import com.app.cent4free.R

class MyApplication : MultiDexApplication() {


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

       /* //trackier
        val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "development")
        TrackierSDK.initialize(sdkConfig)*/

    }

    fun getNetComponent(): ApiComponent? {
        return mApiComponent
    }
}