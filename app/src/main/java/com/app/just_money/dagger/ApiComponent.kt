package com.app.just_money.dagger

import com.app.just_money.available.AvailableFragment
import com.app.just_money.in_progress.InProgressFragment
import com.app.just_money.login.LoginFragment
import com.app.just_money.my_wallet.completed.CompletedFragment
import com.app.just_money.my_wallet.faq.FaqFragment
import com.app.just_money.my_wallet.setting.EditProfileFragment
import com.app.just_money.my_wallet.setting.HelpUsFragment
import com.app.just_money.my_wallet.setting.MyProfileFragment
import com.app.just_money.my_wallet.setting.SettingsNewFragment
import com.app.just_money.offer_details.OfferDetailsFragment
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
    fun inject(offerDetailsFragment: OfferDetailsFragment)
    fun inject(inProgressFragment: InProgressFragment)
    fun inject(completedFragment: CompletedFragment)
    fun inject(helpUsFragment: HelpUsFragment)
    fun inject(myProfileFragment: MyProfileFragment)
    fun inject(editProfileFragment: EditProfileFragment)
    fun inject(settingsNewFragment: SettingsNewFragment)

}