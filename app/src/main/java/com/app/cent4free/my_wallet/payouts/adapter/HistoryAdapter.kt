package com.app.cent4free.my_wallet.payouts.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.databinding.RowItemHistoryBinding
import com.app.cent4free.my_wallet.payouts.model.Payout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.ExperimentalTime

class HistoryAdapter(
    private val context: FragmentActivity?,
    private val dataList: ArrayList<Payout>,
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_item_history, parent, false))
    }

    override fun getItemCount(): Int = dataList.size
    @SuppressLint("SimpleDateFormat")
    @ExperimentalTime
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = dataList[position]

        val dateStr = decrypt(eachListData.datetime.toString())
        val df = SimpleDateFormat("MMM dd, yyyy HH:mm:ss a", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date: Date? = df.parse(dateStr)
        df.timeZone = TimeZone.getDefault()
        val timeFormatter = SimpleDateFormat("h:mm a")
        if (date == null){
            holder.mBinding?.txtTime?.text = "-"
            return
        }
        val displayValue = timeFormatter.format(date)


        holder.mBinding?.txtTime?.text = displayValue
        holder.mBinding?.txtDate?.text = decrypt(eachListData.date.toString())
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

    fun addData(list: List<Payout>?) {
        this.dataList.addAll(list!!)
        notifyDataSetChanged()
    }

    fun clear(){
        this.dataList.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: RowItemHistoryBinding? = DataBindingUtil.bind(itemView)
    }
}