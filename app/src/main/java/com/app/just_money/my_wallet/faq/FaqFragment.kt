package com.app.just_money.my_wallet.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.just_money.R
import com.app.just_money.databinding.FragmentFaqBinding
import com.app.just_money.my_wallet.faq.adapter.FaqAdapter

class FaqFragment : Fragment() {

    private val faqList: List<String> = ArrayList()
    private lateinit var faqAdapter: FaqAdapter
    private lateinit var mBinding: FragmentFaqBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_faq, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    private fun setAdapter() {
        faqAdapter = FaqAdapter(activity!!, faqList)
        mBinding.rvFaq.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvFaq.adapter = faqAdapter
        faqAdapter.notifyDataSetChanged()
    }

}