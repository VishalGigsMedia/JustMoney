package com.app.just_money.in_progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.just_money.R
import com.app.just_money.available.adapter.QuickDealsAdapter
import com.app.just_money.databinding.FragmentInProgressBinding
import com.app.just_money.in_progress.adapter.InProgressAdapter

class InProgressFragment : Fragment() {

    private val blogList: List<String> = ArrayList()
    private lateinit var quickDealsAdapter: QuickDealsAdapter
    private lateinit var inProgressAdapter: InProgressAdapter
    private lateinit var mBinding: FragmentInProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_in_progress, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        blogList.indexOf(0)
        inProgressAdapter = InProgressAdapter(activity!!, blogList)
        mBinding.rvInProgressDeals.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvInProgressDeals.adapter = inProgressAdapter
        inProgressAdapter.notifyDataSetChanged()

        quickDealsAdapter = QuickDealsAdapter(activity!!, blogList)
        mBinding.rvQuickDeals.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvQuickDeals.adapter = quickDealsAdapter
        quickDealsAdapter.notifyDataSetChanged()
    }

}