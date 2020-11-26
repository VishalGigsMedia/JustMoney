package com.app.just_money.dagger

import android.app.Application
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
class AppModule(var mApplication: MyApplication) {

    /* private var mApplication: Application? = null

     fun AppModule(mApplication: Application?) {
         this.mApplication = mApplication
     }*/

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    fun provideApiInterface(retrofit: Retrofit): API {
        return retrofit.create(API::class.java)
    }
}