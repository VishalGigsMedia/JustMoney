package com.app.just_money.my_wallet.payouts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.databinding.RowItemHistoryBinding
import com.app.just_money.my_wallet.payouts.model.PayoutHistoryModel

class HistoryAdapter(
    private val context: FragmentActivity?,
    private val dataList: List<PayoutHistoryModel.Data>,
    ) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_item_history, parent, false))
    }

    override fun getItemCount(): Int = dataList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = dataList[position]

        //1 = Success, 2 = pending, = failed
        holder.mBinding?.txtDate?.text = DefaultHelper.decrypt(eachListData.createdDate)
        holder.mBinding?.txtTime?.text = DefaultHelper.decrypt(eachListData.createdTime)
        holder.mBinding?.txtTitle?.text = DefaultHelper.decrypt(eachListData.name)
        when (DefaultHelper.decrypt(eachListData.status)) {
            "1" -> holder.mBinding?.txtDescription?.text = DefaultHelper.decrypt("Paid")
            "2" -> holder.mBinding?.txtDescription?.text = DefaultHelper.decrypt("Pending")
            "3" -> holder.mBinding?.txtDescription?.text = DefaultHelper.decrypt("Failed")
        }

        holder.mBinding?.txtWiningAmount?.text = DefaultHelper.decrypt(eachListData.chPoints)
        if (eachListData.appIcon.isNotEmpty()) {
            if (context != null) {
                DefaultHelper.loadImage(context,DefaultHelper.decrypt(eachListData.appIcon),
                    holder.mBinding?.ivPayPal!!,
                    ContextCompat.getDrawable(context, R.drawable.ic_logo)!!,
                    ContextCompat.getDrawable(context, R.drawable.ic_logo)!!)
            }
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: RowItemHistoryBinding? = DataBindingUtil.bind(itemView)
    }
}