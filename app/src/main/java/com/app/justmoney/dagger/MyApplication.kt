package com.app.justmoney.dagger

import android.app.Application

class MyApplication : Application() {


    init {
        instance = this
    }

    companion object {
        lateinit var instance: MyApplication
        val TAG = MyApplication::class.java.getSimpleName()
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
        mApiComponent = DaggerApiComponent.builder()
            .appModule(AppModule(this))
            .apiModule(ApiModule("https://downloadandwin.co.in/public/api/"))
            .build()
        mApiComponent!!.inject(this)
    }

   public fun getNetComponent(): ApiComponent? {
        return mApiComponent
    }
}