package com.app.just_money.my_wallet.payouts.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper.decrypt
import com.app.just_money.databinding.RowItemHistoryBinding
import com.app.just_money.my_wallet.payouts.model.Payout

class HistoryAdapter(
    private val context: FragmentActivity?,
    private val dataList: List<Payout>,
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_item_history, parent, false))
    }

    override fun getItemCount(): Int = dataList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = dataList[position]

        holder.mBinding?.txtDate?.text = decrypt(eachListData.date.toString())
        holder.mBinding?.txtTime?.text = decrypt(eachListData.time.toString())
        holder.mBinding?.txtTitle?.text = decrypt(eachListData.title.toString())
        holder.mBinding?.txtDescription?.text = decrypt(eachListData.status.toString())
        holder.mBinding?.txtWiningAmount?.text = decrypt(eachListData.points.toString())
        when (holder.mBinding?.txtDescription?.text) {
            "Successful" -> holder.mBinding.txtDescription.setTextColor(Color.GREEN)
            "Failure" -> (holder.mBinding.txtDescription.setTextColor(Color.RED))
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: RowItemHistoryBinding? = DataBindingUtil.bind(itemView)
    }
}