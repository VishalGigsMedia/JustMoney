package com.app.just_money.my_wallet.payouts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.databinding.RowItemPayoutBinding

class PayoutAdapter(
    private val context: FragmentActivity,
    private val faqList: List<String>,
) : RecyclerView.Adapter<PayoutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_item_payout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: RowItemPayoutBinding? = DataBindingUtil.bind(itemView)
    }
}