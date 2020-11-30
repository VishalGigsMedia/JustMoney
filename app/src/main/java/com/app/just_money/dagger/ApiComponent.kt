package com.app.just_money.dagger

import com.app.just_money.available.AvailableFragment
import com.app.just_money.login.LoginFragment
import com.app.just_money.my_wallet.faq.FaqFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class])
public interface ApiComponent {

    //public fun retrofit(): Retrofit
    fun inject(application: MyApplication)
    fun inject(loginFragment: LoginFragment)
    fun inject(availableFragment: AvailableFragment)
    fun inject(faqFragment: FaqFragment)

}