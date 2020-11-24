package com.app.justmoney.available

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.justmoney.R
import com.app.justmoney.available.adapter.PopularDealsAdapter
import com.app.justmoney.available.adapter.QuickDealsAdapter
import com.app.justmoney.dagger.API
import com.app.justmoney.dagger.InputParams
import com.app.justmoney.dagger.MyApplication
import com.app.justmoney.databinding.FragmentAvailableBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AvailableFragment : Fragment() {

    @Inject
    lateinit var api: API

    private val blogList: List<String> = ArrayList()
    private lateinit var quickDealsAdapter: QuickDealsAdapter
    private lateinit var popularDealsAdapter: PopularDealsAdapter
    private lateinit var mBinding: FragmentAvailableBinding
    private var timer: CountDownTimer? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_available, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyApplication.instance.getNetComponent()?.inject(this)
        getFaq(context!!, api)
        setTimer()
        setAdapter()

    }

    private fun setAdapter() {
        popularDealsAdapter = PopularDealsAdapter(activity!!, blogList)
        mBinding.rvPopular.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvPopular.adapter = popularDealsAdapter
        popularDealsAdapter.notifyDataSetChanged()

        quickDealsAdapter = QuickDealsAdapter(activity!!, blogList)
        mBinding.rvQuickDeals.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvQuickDeals.adapter = quickDealsAdapter
        quickDealsAdapter.notifyDataSetChanged()
    }

    private fun setTimer() {
        timer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                println("millisUntilFinished: $millisUntilFinished")
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
        (timer as CountDownTimer)
    }

    override fun onStop() {
        super.onStop()
        (timer as CountDownTimer)
    }

    private fun getFaq(activity: Context, api: API): MutableLiveData<FAQResponse> {
        val mutableLiveData: MutableLiveData<FAQResponse> = MutableLiveData()
        val aboutParams = InputParams()
        aboutParams.lang_code = "en"
        api.getFaq(aboutParams).enqueue(object : Callback<FAQResponse> {
            override fun onResponse(call: Call<FAQResponse>, response: Response<FAQResponse>) {

                if (response.body() != null && response.isSuccessful && response.code() == 200) {
                    println("onResponse:  " + response.body()!!.message.toString())
                    //Log.d("onResponse: ", response.body()!!.message.toString())
                    /* //update token
                     val header = response.headers().get(activity.getString(R.string.token))
                     if (header != null) {
                         UtilsDefault.updateSharedPreferenceString(Constants.JWT_TOKEN, header)
                     } else {
                         UtilsDefault.updateSharedPreferenceString(Constants.JWT_TOKEN, UtilsDefault.getSharedPreferenceString(Constants.JWT_TOKEN).toString())
                     }

                     faqResponse = response.body()
                     mutableLiveData.value = faqResponse*/
                } else {
                    Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FAQResponse>, error: Throwable) {
                println("onFailure: $error")
            }
        })
        return mutableLiveData
    }

}