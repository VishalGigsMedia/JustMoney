package com.app.cent4free.dagger

import com.app.cent4free.available.AvailableFragment
import com.app.cent4free.in_progress.InProgressFragment
import com.app.cent4free.login.LoginFragment
import com.app.cent4free.login.RegisterFragment
import com.app.cent4free.my_wallet.MyWalletFragment
import com.app.cent4free.my_wallet.completed.CompletedFragment
import com.app.cent4free.my_wallet.faq.FaqFragment
import com.app.cent4free.my_wallet.leaderborard.LeaderBoardFragment
import com.app.cent4free.my_wallet.payouts.MyPayoutFragment
import com.app.cent4free.my_wallet.setting.EditProfileFragment
import com.app.cent4free.my_wallet.setting.help.HelpUsFragment
import com.app.cent4free.my_wallet.setting.MyProfileFragment
import com.app.cent4free.my_wallet.setting.SettingsNewFragment
import com.app.cent4free.offer_details.OfferDetailsFragment
import com.app.cent4free.privacy_policy.PrivacyPolicyFragment
import com.app.cent4free.terms_condition.TermsConditionFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class])
interface ApiComponent {

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
    fun inject(registerFragment: RegisterFragment)
    fun inject(myPayoutFragment: MyPayoutFragment)
    fun inject(leaderBoardFragment: LeaderBoardFragment)
    fun inject(myWalletFragment: MyWalletFragment)
    fun inject(termsConditionFragment: TermsConditionFragment)
    fun inject(privacyPolicyFragment: PrivacyPolicyFragment)
}