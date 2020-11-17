package com.app.justmoney.available

import android.app.Dialog
import android.content.Context
import android.graphics.Paint
import android.os.*
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.justmoney.R
import com.app.justmoney.available.adapter.PopularDealsAdapter
import com.app.justmoney.available.adapter.QuickDealsAdapter
import com.app.justmoney.databinding.FragmentAvailableBinding
import java.util.concurrent.TimeUnit


class AvailableFragment : Fragment() {

    private val blogList: List<String> = ArrayList()
    private lateinit var quickDealsAdapter: QuickDealsAdapter
    private lateinit var popularDealsAdapter: PopularDealsAdapter
    private lateinit var mBinding: FragmentAvailableBinding
    private var timer: CountDownTimer? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_available, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     /*   mBinding.txtDealActualAmount.paintFlags =
            mBinding.txtDealActualAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG*/

        setTimer()
        setAdapter()

        mBinding.clDailyRewardValue.setOnClickListener {
          //  showCollectAmountDialog()
        }
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

        println("Milliseconds = $minutes : $seconds")

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

        /*  val minutesValue = "$minVal\nmin"
          val secondValue = "$secVal\nsec"*/
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
}