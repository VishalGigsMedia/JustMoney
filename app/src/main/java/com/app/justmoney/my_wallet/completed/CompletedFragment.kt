package com.app.justmoney.my_wallet.completed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.justmoney.R
import com.app.justmoney.databinding.FragmentCompletedBinding
import com.app.justmoney.in_progress.adapter.InProgressAdapter

class CompletedFragment : Fragment() {

    private val blogList: List<String> = ArrayList()
    private lateinit var inProgressAdapter: InProgressAdapter
    private lateinit var mBinding: FragmentCompletedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        inProgressAdapter = InProgressAdapter(activity!!, blogList)
        mBinding.rvInProgressDeals.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvInProgressDeals.adapter = inProgressAdapter
        inProgressAdapter.notifyDataSetChanged()
    }

}