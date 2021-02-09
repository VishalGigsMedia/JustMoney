package com.app.just_money.available

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.available.adapter.PopularDealsAdapter
import com.app.just_money.available.adapter.QuickDealsAdapter
import com.app.just_money.available.model.AvailableOffer
import com.app.just_money.available.model.FlashOffer
import com.app.just_money.common_helper.*
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentAvailableBinding
import com.app.just_money.my_wallet.faq.FaqFragment
import com.app.just_money.offer_details.OfferDetailsFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_available, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(true)
        init()
        //checkVersion()
        getOffers()
        setListeners()
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    private fun setListeners() {
        mBinding.clDailyRewardValue.setOnClickListener {
            (activity as MainActivity).onClickMyWallet()
        }
    }

    private fun init() {
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(AvailableOfferViewModel::class.java)
        onClicked = this
        onClickedQuickDeals = this

        mBinding.txtDailyRewardValue.text = "0"
        val preferenceHelper = PreferenceHelper(context)
        val jwtToken = preferenceHelper.getJwtToken()
        println("JWT: $jwtToken")
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

        // println("Milliseconds = $minutes : $seconds")

        var minVal: String = ""
        var secVal: String = ""
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

    private fun getOffers() {
        mBinding.shimmerViewContainer.startShimmer()
        viewModel.getOffers(context!!, api).observe(viewLifecycleOwner, { availableOfferModel ->
            mBinding.shimmerViewContainer.stopShimmer()
            mBinding.shimmerViewContainer.visibility = GONE
            mBinding.nsv.visibility = VISIBLE
            run {
                if (availableOfferModel != null) when (availableOfferModel.status) {
                    DefaultKeyHelper.successCode -> {
                        val dailyReward =
                            availableOfferModel.availableOfferData?.dailyRewards.toString()
                        val totalCoins = availableOfferModel.totalCoins.toString()
                        val withdrawn = availableOfferModel.withdrawn.toString()
                        val completed = availableOfferModel.completed.toString()
                        setDailyReward(dailyReward, totalCoins, withdrawn, completed)

                        if (availableOfferModel.availableOfferData?.flashOffer != null) {
                            mBinding.clBestDeal.visibility = VISIBLE
                            setFlashOffer(availableOfferModel.availableOfferData.flashOffer)
                        } else mBinding.clBestDeal.visibility = GONE

                        if (availableOfferModel.availableOfferData?.popular != null) {
                            popularDealsAdapter(availableOfferModel.availableOfferData.popular)
                        } else {
                            mBinding.txtPopular.visibility = GONE
                            mBinding.rvPopular.visibility = GONE
                        }

                        if (availableOfferModel.availableOfferData?.quickDeals != null) {
                            setAdapter(availableOfferModel.availableOfferData.quickDeals!!)
                        } else {
                            mBinding.txtQuickDeals.visibility = GONE
                            mBinding.rvQuickDeals.visibility = GONE
                        }

                        //if no offer available among three types, just showing error screen
                        if (mBinding.clBestDeal.visibility == GONE && mBinding.rvPopular.visibility == GONE && mBinding.rvQuickDeals.visibility == GONE) showErrorScreen()

                    }
                    DefaultKeyHelper.failureCode -> {
                        DefaultHelper.showToast(context!!, DefaultHelper.decrypt(availableOfferModel.message.toString()))
                        showErrorScreen()
                    }
                    DefaultKeyHelper.forceLogoutCode -> {
                        DefaultHelper.forceLogout(activity)
                    }
                    else -> {
                        DefaultHelper.showToast(context!!, DefaultHelper.decrypt(availableOfferModel.message.toString()))
                        showErrorScreen()
                    }
                } else {
                    showErrorScreen()
                }
            }
        })
    }

    private fun showErrorScreen() {
        mBinding.clData.visibility = GONE
        mBinding.llError.visibility = VISIBLE
    }

    private fun popularDealsAdapter(popularList: List<AvailableOffer>) {
        if (popularList.isNotEmpty()) {
            mBinding.txtPopular.visibility = VISIBLE
            mBinding.rvPopular.visibility = VISIBLE

            popularDealsAdapter = PopularDealsAdapter(activity!!, popularList, onClicked!!)
            mBinding.rvPopular.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            mBinding.rvPopular.adapter = popularDealsAdapter
            popularDealsAdapter.notifyDataSetChanged()

        } else {
            mBinding.txtPopular.visibility = GONE
            mBinding.rvPopular.visibility = GONE
        }
    }


    private fun setDailyReward(dailyReward: String, totalCoins: String, withdrawn: String, completed: String) {
        val preferenceHelper = PreferenceHelper(context!!)
        if (dailyReward.isNotEmpty()) {
            mBinding.txtDailyRewardValue.text = DefaultHelper.decrypt(dailyReward)
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
            mBinding.clBestDeal.visibility = View.VISIBLE

            val flashOfferName = DefaultHelper.decrypt(flashOffer[0].name.toString())
            val description = DefaultHelper.decrypt(flashOffer[0].shortDescription.toString())
            val endDate = DefaultHelper.decrypt(flashOffer[0].downloadEndDate.toString())
            /*   val startDate = DefaultHelper.decrypt(flashOffer[0].downloadStartDate.toString())
               val offerId = DefaultHelper.decrypt(flashOffer[0].offerId.toString())
               val offerType = DefaultHelper.decrypt(flashOffer[0].offerType.toString())
               val url = DefaultHelper.decrypt(flashOffer[0].url.toString())
               val trackingLink = DefaultHelper.decrypt(flashOffer[0].originalTrackLink.toString())*/
            val image = DefaultHelper.decrypt(flashOffer[0].image.toString())
            val actualCoins = DefaultHelper.decrypt(flashOffer[0].actualCoins.toString())
            val offerCoins = DefaultHelper.decrypt(flashOffer[0].offerCoins.toString())

            val values: Array<String> = endDate.split(" ").toTypedArray()
            val timeValue: Array<String> = values[1].split(":").toTypedArray()
            /* println("date : " + values[0])
             println("time : " + values[1])
             println("hour : " + timeValue[0])
             println("minute : " + timeValue[1])
             println("second : " + timeValue[2])*/

            mBinding.txtTitle.text = flashOfferName
            mBinding.txtDescription.text = description
            mBinding.txtMinute.text = timeValue[0]
            mBinding.txtSeconds.text = timeValue[1]
            mBinding.txtDealActualAmount.text = actualCoins
            mBinding.txtDealOfferAmount.text = offerCoins
            mBinding.txtRedeemOfferAmount.text = offerCoins

            if (image.isNotEmpty()) {
                Glide.with(context!!).load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(mBinding.ivLogo)
            }

            val minute = timeValue[1].toLong()
            val second = timeValue[2].toLong()
            val convertMinute = TimeUnit.MINUTES.toMillis(minute) //minute.toLong() * 60000
            val convertSecond = TimeUnit.SECONDS.toMillis(second)//second.toLong() * 1000
            val time = convertMinute + convertSecond
            //println("timeTotal : $time")
            if (timer == null) setTimer(time)

            mBinding.txtRedeemOfferAmount.setOnClickListener {
                claimOffer(flashOffer[0].id.toString(), flashOffer[0].url.toString())
            }
            mBinding.txtHaveAQuestion.setOnClickListener {
                openFragment(FaqFragment(), true)
            }
        } else {
            mBinding.clBestDeal.visibility = View.GONE
        }

    }

    private fun setAdapter(quickDeals: List<AvailableOffer>) {
        if (quickDeals.isNotEmpty()) {
            mBinding.txtQuickDeals.visibility = View.VISIBLE
            mBinding.rvQuickDeals.visibility = View.VISIBLE

            quickDealsAdapter = QuickDealsAdapter(activity!!, quickDeals, onClickedQuickDeals!!)
            mBinding.rvQuickDeals.adapter = quickDealsAdapter
            quickDealsAdapter.notifyDataSetChanged()
        } else {
            mBinding.txtQuickDeals.visibility = View.GONE
            mBinding.rvQuickDeals.visibility = View.GONE
        }
    }

    override fun claimOffers(appId: String, url: String) {
        claimOffer(appId, url)
    }

    override fun showOfferDetails(offerId: String, displayId: String) {
        val offerDetailFragment = OfferDetailsFragment()
        val bundle = Bundle()
        bundle.putString(BundleHelper.offerId, offerId)
        bundle.putString(BundleHelper.displayId, displayId)
        offerDetailFragment.arguments = bundle
        openFragment(offerDetailFragment, true)
    }

    override fun getOffers(appId: String, url: String) {
        claimOffer(appId, url)
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (addToBackStack) {
            activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, fragment)
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
        } else {
            activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, fragment)
                ?.commit()
        }
    }

    private fun claimOffer(appId: String, url: String) {
        viewModel.claimOffer(context!!, api, appId).observe(viewLifecycleOwner, { claimOfferModel ->
            if (claimOfferModel != null) {
                if (claimOfferModel.status == DefaultKeyHelper.successCode) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } else DefaultHelper.showToast(context, DefaultHelper.decrypt(claimOfferModel.message.toString()))
            }
        })
    }

    /*private fun checkVersion() {
        viewModel.checkVersion(context!!, api, "").observe(viewLifecycleOwner, { versionModel ->
            if (versionModel != null) {
                when (versionModel.status) {
                    DefaultKeyHelper.successCode -> {
                        *//*DefaultHelper.showToast(
                            context!!,
                            DefaultHelper.decrypt(versionModel.message.toString())
                        )*//*
                    }
                    DefaultKeyHelper.failureCode -> {
                        *//* DefaultHelper.showToast(
                            context!!,
                            DefaultHelper.decrypt(versionModel.message.toString())
                        )*//*
                    }
                }
            }
        })
    }*/
}