package com.app.just_money.available

import android.animation.Animator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.R.string.hour_left
import com.app.just_money.R.string.hours_left
import com.app.just_money.available.adapter.PopularDealsAdapter
import com.app.just_money.available.adapter.QuickDealsAdapter
import com.app.just_money.available.model.*
import com.app.just_money.common_helper.*
import com.app.just_money.common_helper.BundleHelper.displayId
import com.app.just_money.common_helper.BundleHelper.offerId
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentAvailableBinding
import com.app.just_money.my_wallet.faq.FaqFragment
import com.app.just_money.offer_details.OfferDetailsFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_popup_offer.view.*
import kotlinx.android.synthetic.main.update_verstion_dialog.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


private var timer: CountDownTimer? = null

class AvailableFragment : Fragment(), PopularDealsAdapter.OnClickedPopularDeals,
    QuickDealsAdapter.OnClickedQuickDeals {

    @Inject
    lateinit var api: API
    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var quickDealsAdapter: QuickDealsAdapter
    private lateinit var popularDealsAdapter: PopularDealsAdapter
    private lateinit var viewModel: AvailableOfferViewModel
    private lateinit var mBinding: FragmentAvailableBinding
    private var onClicked: PopularDealsAdapter.OnClickedPopularDeals? = null
    private var onClickedQuickDeals: QuickDealsAdapter.OnClickedQuickDeals? = null
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_available, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(true)
        preferenceHelper = PreferenceHelper(context)

        init()
        setListeners()
        DefaultHelper.playCustomSound(context, R.raw.load_dashboard)

        mBinding.swipe.setOnRefreshListener { getOffers() }
    }

    private fun setListeners() {
        mBinding.clDailyRewardValue.setOnClickListener {
            claimReward(DefaultHelper.encrypt(mBinding.txtDailyRewardValue.text.toString()))
        }
    }

    private fun init() {
        MyApplication.instance.getNetComponent()?.inject(this)
        //viewModel = ViewModelProvider(this).get(AvailableOfferViewModel::class.java)
        onClicked = this
        onClickedQuickDeals = this
        getIPAddress()
    }

    private fun getOffers() {
        showShimmer()
        viewModel.getOffers(context, api).observe(viewLifecycleOwner, { availableOfferModel ->
            hideShimmer()
            if (mBinding.swipe.isRefreshing) {
                mBinding.swipe.isRefreshing = false
            }
            run {
                if (availableOfferModel != null) {
                    when (availableOfferModel.status) {
                        DefaultKeyHelper.successCode -> {
                            TrackingEvents.trackOfferList()
                            val offerData = availableOfferModel.availableOfferData
                            val dailyReward = offerData?.dailyRewards.toString()
                            val rewardRemainingTime = offerData?.reward_remaining_time
                            val totalCoins = availableOfferModel.totalCoins.toString()
                            val withdrawn = availableOfferModel.withdrawn.toString()
                            val completed = availableOfferModel.completed.toString()
                            //Daily Reward
                            setDailyReward(dailyReward, rewardRemainingTime, totalCoins, withdrawn, completed)

                            //popup offer
                            if (offerData?.popup != null && (activity as MainActivity).popup == 0) {
                                showPopupOffer(offerData.popup)
                            }

                            //flash offer
                            if (offerData?.flashOffer != null) {
                                mBinding.clBestDeal.visibility = VISIBLE
                                setFlashOffer(offerData.flashOffer)
                            } else mBinding.clBestDeal.visibility = GONE

                            //popular offer
                            if (offerData?.popular != null) {
                                popularDealsAdapter(offerData.popular)
                            } else {
                                mBinding.txtPopular.visibility = GONE
                                mBinding.rvPopular.visibility = GONE
                            }

                            //quick deals offer
                            if (offerData?.quickDeals != null) {
                                setAdapter(offerData.quickDeals)
                            } else {
                                mBinding.txtQuickDeals.visibility = GONE
                                mBinding.rvQuickDeals.visibility = GONE
                            }

                            //if no offer available among three types, just showing error screen
                            if (mBinding.clBestDeal.visibility == GONE && mBinding.rvPopular.visibility == GONE && mBinding.rvQuickDeals.visibility == GONE) showErrorScreen()
                        }
                        DefaultKeyHelper.failureCode -> {
                            DefaultHelper.showToast(context,
                                DefaultHelper.decrypt(availableOfferModel.message.toString()))
                            showErrorScreen()
                        }
                        DefaultKeyHelper.forceLogoutCode -> {
                            DefaultHelper.forceLogout(activity)
                        }
                        else -> {
                            DefaultHelper.showToast(context,
                                DefaultHelper.decrypt(availableOfferModel.message.toString()))
                            showErrorScreen()
                        }
                    }
                } else showErrorScreen()
            }
        })
    }

    override fun onResume() {
        super.onResume();checkVersion()
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }


    private fun setTimer(milliseconds: Long) {
        timer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // println("millisUntilFinished: $millisUntilFinished")
                updateTimerUI(millisUntilFinished)
            }

            override fun onFinish() {
                (timer as CountDownTimer).cancel()
            }
        }
        (timer as CountDownTimer).start()
    }

    private fun updateTimerUI(milliseconds: Long) {
        // long minutes = (milliseconds / 1000) / 60;
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)

        // long seconds = (milliseconds / 1000);
        //val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        val seconds = milliseconds / 1000 % 60

        /* println("Milliseconds = $minutes : $seconds") */

        val minVal: String
        val secVal: String
        minVal = if (minutes < 10) {
            "0$minutes"
        } else {
            minutes.toString()
        }

        secVal = if (seconds < 10) {
            "0$seconds"
        } else {
            seconds.toString()
        }

        mBinding.txtMinute.text = minVal
        mBinding.txtSeconds.text = secVal
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) {
            (timer as CountDownTimer).cancel()
            timer = null
        }
    }

    private fun showErrorScreen() {
        mBinding.clData.visibility = GONE
        mBinding.llError.visibility = VISIBLE
    }

    private fun popularDealsAdapter(popularList: List<AvailableOffer>) {
        if (popularList.isNotEmpty()) {
            mBinding.txtPopular.visibility = VISIBLE
            mBinding.rvPopular.visibility = VISIBLE

            popularDealsAdapter = PopularDealsAdapter(activity, popularList, onClicked)
            mBinding.rvPopular.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            mBinding.rvPopular.adapter = popularDealsAdapter
            popularDealsAdapter.notifyDataSetChanged()

        } else {
            mBinding.txtPopular.visibility = GONE
            mBinding.rvPopular.visibility = GONE
        }
    }


    private fun setDailyReward(dailyReward: String, rewardRemainingTime: RewardRemainingTime?, totalCoins: String,
        withdrawn: String, completed: String) {
        val preferenceHelper = PreferenceHelper(context)
        var hours = DefaultHelper.decrypt(rewardRemainingTime?.hours.toString())
        var minutes = DefaultHelper.decrypt(rewardRemainingTime?.minutes.toString())
        val seconds = DefaultHelper.decrypt(rewardRemainingTime?.seconds.toString())
        if (hours.toInt() == 0 && minutes.toInt() == 0 && seconds.toInt() == 0) {
            mBinding.clDailyRewardValue.visibility = VISIBLE
            mBinding.tvTimeLeft.visibility = GONE
            if (dailyReward.isNotEmpty()) {
                mBinding.txtDailyRewardValue.text = DefaultHelper.decrypt(dailyReward)
            }
        } else {
            mBinding.clDailyRewardValue.visibility = GONE
            mBinding.tvTimeLeft.visibility = VISIBLE
            if (hours.length == 1) hours = "0$hours"//manipulation
            if (minutes.length == 1) minutes = "0$minutes"//manipulation

            val time = if (hours == "00") "$hours:$minutes ${getString(hour_left)}"
            else "$hours:$minutes ${getString(hours_left)}"
            mBinding.tvTimeLeft.text = time
        }

        if (totalCoins.isNotEmpty()) {
            preferenceHelper.setTotalCoins(totalCoins)
        }
        if (completed.isNotEmpty()) {
            preferenceHelper.setCompleted(completed)
        }
        if (withdrawn.isNotEmpty()) {
            preferenceHelper.setWithdrawn(withdrawn)
        }
    }

    private fun setFlashOffer(flashOffer: List<FlashOffer>?) {
        if (flashOffer != null && flashOffer.isNotEmpty()) {
            mBinding.clBestDeal.visibility = VISIBLE

            val flashOfferName = DefaultHelper.decrypt(flashOffer[0].name.toString())
            val description = DefaultHelper.decrypt(flashOffer[0].shortDescription.toString())
            val endDate = DefaultHelper.decrypt(flashOffer[0].downloadEndDate.toString())
            /*val startDate = DefaultHelper.decrypt(flashOffer[0].downloadStartDate.toString())*/
            /*val offerId = DefaultHelper.decrypt(flashOffer[0].offerId.toString())*/
            /*val offerType = DefaultHelper.decrypt(flashOffer[0].offerType.toString())*/
            /*val url = DefaultHelper.decrypt(flashOffer[0].url.toString())*/
            /*val trackingLink = DefaultHelper.decrypt(flashOffer[0].originalTrackLink.toString())*/
            val image = DefaultHelper.decrypt(flashOffer[0].image.toString())
            val actualCoins = DefaultHelper.decrypt(flashOffer[0].actualCoins.toString())
            val offerCoins = DefaultHelper.decrypt(flashOffer[0].offerCoins.toString())

            val values: Array<String> = endDate.split(" ").toTypedArray()
            val timeValue: Array<String> = values[1].split(":").toTypedArray()

            mBinding.txtTitle.text = flashOfferName
            mBinding.txtDescription.text = description
            mBinding.txtMinute.text = timeValue[0]
            mBinding.txtSeconds.text = timeValue[1]
            mBinding.txtDealActualAmount.text = actualCoins
            mBinding.txtDealOfferAmount.text = offerCoins
            mBinding.txtRedeemOfferAmount.text = offerCoins

            if (image.isNotEmpty()) {
                Glide.with(context!!).load(image).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .into(mBinding.ivLogo)
            }

            val minute = timeValue[1].toLong()
            val second = timeValue[2].toLong()
            val convertMinute = TimeUnit.MINUTES.toMillis(minute) //minute.toLong() * 60000
            val convertSecond = TimeUnit.SECONDS.toMillis(second)//second.toLong() * 1000
            val time = convertMinute + convertSecond
            //println("timeTotal : $time")
            if (timer == null) setTimer(time)

            mBinding.txtRedeemOfferAmount.setOnClickListener {
                claimOffer(flashOffer[0].id.toString(), DefaultHelper.decrypt(flashOffer[0].url.toString()))
            }
            mBinding.txtHaveAQuestion.setOnClickListener {
                openFragment(FaqFragment())
            }
        } else {
            mBinding.clBestDeal.visibility = GONE
        }

    }

    private fun setAdapter(quickDeals: List<AvailableOffer>?) {
        if (quickDeals?.isNotEmpty() == true) {
            mBinding.txtQuickDeals.visibility = VISIBLE
            mBinding.rvQuickDeals.visibility = VISIBLE

            quickDealsAdapter = QuickDealsAdapter(activity, quickDeals, onClickedQuickDeals)
            mBinding.rvQuickDeals.adapter = quickDealsAdapter
            quickDealsAdapter.notifyDataSetChanged()
        } else {
            mBinding.txtQuickDeals.visibility = GONE
            mBinding.rvQuickDeals.visibility = GONE
        }
    }

    override fun claimOffers(appId: String, url: String) {
        claimOffer(appId, url)
    }

    override fun getOffers(appId: String, url: String) {
        claimOffer(appId, url)
    }

    override fun showOfferDetails(offerId: String, displayId: String) {
        val offerDetailFragment = OfferDetailsFragment()
        val bundle = Bundle()
        bundle.putString(BundleHelper.offerId, offerId)
        bundle.putString(BundleHelper.displayId, displayId)
        offerDetailFragment.arguments = bundle
        openFragment(offerDetailFragment)
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        if (addToBackStack) {
            activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, fragment)
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
        } else {
            activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, fragment)?.commit()
        }
    }

    private fun claimOffer(appId: String, url: String) {
        viewModel.claimOffer(context, api, appId).observe(viewLifecycleOwner, { claimOfferModel ->
            if (claimOfferModel != null) {
                if (claimOfferModel.status == DefaultKeyHelper.successCode && url != "") {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } else DefaultHelper.showToast(context, DefaultHelper.decrypt(claimOfferModel.message.toString()))
            }
        })
    }

    private fun checkVersion() {
        viewModel.checkVersion(context, api).observe(viewLifecycleOwner, { versionModel ->
            if (versionModel != null) {
                when (versionModel.status) {
                    DefaultKeyHelper.successCode -> {
                        var updateVersion: Long = 0
                        val title = DefaultHelper.decrypt(versionModel.title.toString())
                        val playStoreUrl = DefaultHelper.decrypt(versionModel.data?.url.toString())
                        if (versionModel.data?.version != null) {
                            updateVersion = (DefaultHelper.decrypt(versionModel.data.version.toString())).toLong()
                        }
                        val applicationVersion = DefaultHelper.getVersionCode()
                        val msg = DefaultHelper.decrypt(versionModel.message.toString())

                        updateApplicationDialog(updateVersion, applicationVersion, title, msg, playStoreUrl)
                    }
                    DefaultKeyHelper.failureCode -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(versionModel.message.toString()))
                    }
                }
            }
        })
    }

    private fun claimReward(rewardAmount: String) {
        viewModel.claimReward(context, api, rewardAmount).observe(viewLifecycleOwner, { model ->
            if (model != null) {
                when (model.status) {
                    DefaultKeyHelper.successCode -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(model.message.toString()))
                        showCoinAnimation(mBinding.coinAnimation)
                        DefaultHelper.playCustomSound(context, R.raw.reward)
                        getOffers()
                        TrackingEvents.trackDailyReward(rewardAmount)
                    }
                    DefaultKeyHelper.failureCode -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(model.message.toString()))
                    }
                }
            } else DefaultHelper.showToast(context, getString(R.string.somethingWentWrong))
        })
    }

    private fun getIPAddress() {
        viewModel.getIPAddress(context, api).observe(viewLifecycleOwner, fun(ipAddressModel: IpAddressModel?) {
            if (ipAddressModel != null) {
                if (ipAddressModel.ip!==null&&ipAddressModel.ip!="") {
                    preferenceHelper.setIpAddress(ipAddressModel.ip)
                    getOffers()
                }else{
                    DefaultHelper.showToast(context,getString(R.string.somethingWentWrong))
                    activity?.finish()
                }
            }
        })
    }

    private fun updateApplicationDialog(updateVersion: Long, applicationVersion: Long?, title: String,
        message: String, url: String) {
        println("applicationVersion : $applicationVersion   updateVersion : $updateVersion")
        if (applicationVersion != null) {
            if (applicationVersion < updateVersion) {
                showDialog(title, message, url)
            }
        }
    }

    private fun showDialog(title: String, message: String, url: String) {
        if (context != null) {
            val dialog = BottomSheetDialog(context!!, R.style.AppBottomSheetDialogTheme)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.update_verstion_dialog)
            dialog.window?.setGravity(Gravity.BOTTOM)
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)

            if (title.isNotEmpty()) {
                dialog.txtTitle.text = title
            }
            if (message.isNotEmpty()) {
                dialog.txtMessage.text = message
            }

            dialog.txtUpdateApplication.setOnClickListener {
                //  startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dne.rewardapp&reviewId")))
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun showCoinAnimation(view: LottieAnimationView) {
        view.visibility = VISIBLE
        view.playAnimation()
        view.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.visibility = GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
    }

    private fun showPopupOffer(popup: Popup) {
        if (context == null) return
        val popupOfferView = LayoutInflater.from(activity).inflate(R.layout.layout_popup_offer, null)
        val popupOfferBuilder = AlertDialog.Builder(context!!).setView(popupOfferView)

        val alert = popupOfferBuilder.show()//can use this instance for dismissing
        val window: Window = alert.window!!
        window.setLayout(550, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        alert.show()
        (activity as MainActivity).popup = 1
        //setting  values
        DefaultHelper.loadImage(context, DefaultHelper.decrypt(popup.image.toString()),
            popupOfferView.ivOfferImage, ContextCompat.getDrawable(context!!, R.drawable.ic_love_app),
            ContextCompat.getDrawable(context!!, R.drawable.ic_logo))
        val offerCoins = DefaultHelper.decrypt(popup.offer_coins.toString())
        val actualCoins = DefaultHelper.decrypt(popup.actual_coins.toString())
        popupOfferView.txtOfferCoins.text = offerCoins
        popupOfferView.txtActualCoins.text = actualCoins
        popupOfferView.txtSaveCoins.text = "Earn Extra " + (offerCoins.toInt() - actualCoins.toInt())
        popupOfferView.txtDescription.text = DefaultHelper.decrypt(popup.short_description.toString())
        popupOfferView.txtOfferExpiry.text =
            "Offer expires " + DefaultHelper.decrypt(popup.download_end_date.toString())

        popupOfferView.tvSeeOfferDetails.setOnClickListener {
            val offerDetails = OfferDetailsFragment()
            val bundle = Bundle()
            bundle.putString(offerId, popup.id.toString())
            bundle.putString(displayId, popup.id.toString())
            offerDetails.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, offerDetails)
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
            alert.dismiss()
        }
    }
    private fun hideShimmer() {
        mBinding.shimmerViewContainer.stopShimmer()
        mBinding.shimmerViewContainer.visibility = GONE
        mBinding.nsv.visibility = VISIBLE
    }

    private fun showShimmer() {
        mBinding.shimmerViewContainer.visibility = VISIBLE
        mBinding.nsv.visibility = GONE
        mBinding.shimmerViewContainer.startShimmer()
    }


}