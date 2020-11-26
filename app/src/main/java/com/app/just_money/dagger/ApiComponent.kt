package com.app.just_money.dagger

import com.app.just_money.available.AvailableFragment
import com.app.just_money.login.LoginFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class])
public interface ApiComponent {

    //public fun retrofit(): Retrofit
    public fun inject(application: MyApplication)
    public fun inject(loginFragment: LoginFragment)
    public fun inject(availableFragment: AvailableFragment)
}