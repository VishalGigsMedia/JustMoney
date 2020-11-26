package com.app.just_money.my_wallet.completed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.databinding.RowItemCompletedPinkBgBinding
import com.app.just_money.databinding.RowItemCompletedWhiteBgBinding

class CompletedAdapter(
    private val context: FragmentActivity,
    private val popularList: List<String>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val first = 0
        private const val second = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            first -> {
                return TypeFirstViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.row_item_completed_pink_bg,
                        parent,
                        false
                    )
                )
            }
            second -> {
                return TypeSecondViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.row_item_completed_white_bg,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return TypeFirstViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.row_item_completed_pink_bg,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return 5//popularList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is TypeFirstViewHolder) {
            /*val eachListData = popularList[position]
            holder.mBinding?.data = eachListData*/

        } else if (holder is TypeSecondViewHolder) {

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            first
        } else {
            second
        }
    }

    class TypeFirstViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingCompletedPinkBgBinding: RowItemCompletedPinkBgBinding? = DataBindingUtil.bind(itemView)
    }

    class TypeSecondViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingCompletedWhiteBgBinding: RowItemCompletedWhiteBgBinding? =
            DataBindingUtil.bind(itemView)
    }

}