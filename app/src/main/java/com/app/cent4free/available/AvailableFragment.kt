package com.app.cent4free.available

import android.animation.Animator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request.Method.GET
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.cent4free.MainActivity
import com.app.cent4free.R
import com.app.cent4free.R.string.hour_left
import com.app.cent4free.R.string.hours_left
import com.app.cent4free.available.adapter.PopularDealsAdapter
import com.app.cent4free.available.adapter.QuickDealsAdapter
import com.app.cent4free.available.model.AvailableOffer
import com.app.cent4free.available.model.FlashOffer
import com.app.cent4free.available.model.Popup
import com.app.cent4free.available.model.RewardRemainingTime
import com.app.cent4free.common_helper.*
import com.app.cent4free.common_helper.BundleHelper.offerId
import com.app.cent4free.common_helper.BundleHelper.offer_trackier_id
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.common_helper.DefaultHelper.forceLogout
import com.app.cent4free.common_helper.DefaultHelper.getVersionCode
import com.app.cent4free.common_helper.DefaultHelper.playCustomSound
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.common_helper.DefaultKeyHelper.failureCode
import com.app.cent4free.common_helper.DefaultKeyHelper.forceLogoutCode
import com.app.cent4free.common_helper.DefaultKeyHelper.playStoreLink
import com.app.cent4free.common_helper.DefaultKeyHelper.successCode
import com.app.cent4free.common_helper.TrackingEvents.trackDailyReward
import com.app.cent4free.common_helper.TrackingEvents.trackOfferList
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.MyApplication
import com.app.cent4free.databinding.FragmentAvailableBinding
import com.app.cent4free.my_wallet.faq.FaqFragment
import com.app.cent4free.offer_details.OfferDetailsFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_popup_offer.view.*
import kotlinx.android.synthetic.main.update_verstion_dialog.*
import org.json.JSONObject
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
    private var appVersion: Long = -1
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

        mBinding.swipe.setOnRefreshListener { getIPAddress() }
    }

    private fun setListeners() {
        mBinding.clDailyRewardValue.setOnClickListener {
            claimReward(DefaultHelper.encrypt(mBinding.txtDailyRewardValue.text.toString()))
        }
    }

    private fun init() {
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(AvailableOfferViewModel::class.java)
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
                        successCode -> {
                            trackOfferList()
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

                            //version
                            if(availableOfferModel.availableOfferData?.app_version != null) {
                                appVersion = decrypt(availableOfferModel.availableOfferData.app_version).toLong()
                                checkUpdate()
                            }

                        }
                        failureCode -> {
                            showToast(context, decrypt(availableOfferModel.message.toString()))
                            showErrorScreen()
                        }
                        forceLogoutCode -> {
                            forceLogout(activity)
                        }
                        else -> {
                            showToast(context, decrypt(availableOfferModel.message.toString()))
                            showErrorScreen()
                        }
                    }
                } else showErrorScreen()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        checkUpdate()
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
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        val seconds = milliseconds / 1000 % 60

        mBinding.txtMinute.text = if (minutes < 10) "0$minutes" else minutes.toString()
        mBinding.txtSeconds.text = if (seconds < 10) "0$seconds" else seconds.toString()
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
        var hours = decrypt(rewardRemainingTime?.hours.toString())
        var minutes = decrypt(rewardRemainingTime?.minutes.toString())
        val seconds = decrypt(rewardRemainingTime?.seconds.toString())
        println("hbefhbde $hours $minutes $seconds")
        if (hours.toInt() == 0 && minutes.toInt() == 0 && seconds.toInt() == 0) {
            mBinding.clDailyRewardValue.visibility = VISIBLE
            mBinding.tvTimeLeft.visibility = GONE
            if (dailyReward.isNotEmpty()) {
                mBinding.txtDailyRewardValue.text = decrypt(dailyReward)
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

        if (totalCoins.isNotEmpty()) preferenceHelper.setTotalCoins(totalCoins)
        if (completed.isNotEmpty()) preferenceHelper.setCompleted(completed)
        if (withdrawn.isNotEmpty()) preferenceHelper.setWithdrawn(withdrawn)
    }

    private fun setFlashOffer(flashOffer: List<FlashOffer>?) {
        if (flashOffer != null && flashOffer.isNotEmpty()) {
            mBinding.clBestDeal.visibility = VISIBLE

            val flashOfferName = decrypt(flashOffer[0].name.toString())
            val description = decrypt(flashOffer[0].shortDescription.toString())
            val endDate = decrypt(flashOffer[0].downloadEndDate.toString())
            val image = decrypt(flashOffer[0].image.toString())
            val actualCoins = decrypt(flashOffer[0].actualCoins.toString())
            val offerCoins = decrypt(flashOffer[0].offerCoins.toString())

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
                claimOffer(flashOffer[0].id.toString(), decrypt(flashOffer[0].url.toString()))
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

    override fun claimOffers(offerId: String, url: String) {
        claimOffer(offerId, url)
    }

    override fun getOffers(offer_id: String, url: String) {
        claimOffer(offer_id, url)
    }

    override fun showOfferDetails(offerId: String,offer_trackier_id:String) {
        val offerDetailFragment = OfferDetailsFragment()
        val bundle = Bundle()
        bundle.putString(BundleHelper.offerId, offerId)
        bundle.putString(BundleHelper.offer_trackier_id, offer_trackier_id)
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

    private fun claimOffer(offer_id: String, url: String) {
        viewModel.claimOffer(context, api, offer_id).observe(viewLifecycleOwner, { claimOfferModel ->
            if (claimOfferModel != null) {
                if (claimOfferModel.status == successCode && url != "") {
                    val intent = Intent(Intent.ACTION_VIEW)
                    Log.d("url", "claimOffer: $url")
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } else showToast(context, decrypt(claimOfferModel.message.toString()))
            }
        })
    }

    private fun claimReward(rewardAmount: String) {
        viewModel.claimReward(context, api, rewardAmount).observe(viewLifecycleOwner, { model ->
            if (model != null) {
                when (model.status) {
                    successCode -> {
                        showToast(context, decrypt(model.message.toString()))
                        playCustomSound(context, R.raw.reward)
                        showCoinAnimation(mBinding.coinAnimation)
                        trackDailyReward(rewardAmount)
                    }
                    failureCode -> {
                        showToast(context, decrypt(model.message.toString()))
                    }
                }
            } else showToast(context, getString(R.string.somethingWentWrong))
        })
    }

    private fun getIPAddress() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val url = "http://ip-api.com/json/"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(GET, url, { response ->
            // Display the first 500 characters of the response string.
            val finalResponse = JSONObject(response)
            preferenceHelper.setIpAddress(finalResponse.getString("query"))
            preferenceHelper.setUserCountry(finalResponse.getString("country"))
            preferenceHelper.setUserCountryCode(finalResponse.getString("countryCode"))
            preferenceHelper.setUserState(finalResponse.getString("regionName"))
            preferenceHelper.setUserStateCode(finalResponse.getString("region"))
            preferenceHelper.setUserCity(finalResponse.getString("city"))
            getOffers()
        }, {
            showToast(context, getString(R.string.somethingWentWrong))
            activity?.finish()
        })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }



    private fun showCoinAnimation(view: LottieAnimationView) {
        view.visibility = VISIBLE
        view.playAnimation()
        view.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                view.visibility = GONE
                getOffers()
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
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
        DefaultHelper.loadImage(context, decrypt(popup.image.toString()), popupOfferView.ivOfferImage,
            ContextCompat.getDrawable(context!!, R.drawable.ic_love_app),
            ContextCompat.getDrawable(context!!, R.drawable.logo_without_text))
        val offerCoins = decrypt(popup.offer_coins.toString())
        val actualCoins = decrypt(popup.actual_coins.toString())
        popupOfferView.txtOfferCoins.text = offerCoins
        popupOfferView.txtActualCoins.text = actualCoins
        popupOfferView.txtSaveCoins.text = "Earn Extra " + (offerCoins.toInt() - actualCoins.toInt())
        popupOfferView.txtDescription.text = decrypt(popup.short_description.toString())
        popupOfferView.txtOfferExpiry.text = "Offer expires " + decrypt(popup.download_end_date.toString())

        popupOfferView.tvSeeOfferDetails.setOnClickListener {
            val offerDetails = OfferDetailsFragment()
            val bundle = Bundle()
            bundle.putString(offerId, popup.id.toString())
            bundle.putString(offer_trackier_id, popup.offer_trackier_id.toString())
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

    private fun checkUpdate() {
        if (appVersion == -1L || context == null) return
        if (getVersionCode() < appVersion) {
            val dialog = BottomSheetDialog(context!!, R.style.AppBottomSheetDialogTheme)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.update_verstion_dialog)
            dialog.window?.setGravity(Gravity.BOTTOM)
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)

            dialog.txtTitle.text = getString(R.string.update_available)
            dialog.txtMessage.text = getString(R.string.older_version_text)

            dialog.txtUpdateApplication.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }
            dialog.show()
        }
    }


}