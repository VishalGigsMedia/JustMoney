package com.app.justmoney.dagger

import com.app.justmoney.available.AvailableFragment
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class])
public interface ApiComponent {

    //public fun retrofit(): Retrofit
    public fun inject(application: MyApplication)
    public fun inject(availableFragment: AvailableFragment)
}