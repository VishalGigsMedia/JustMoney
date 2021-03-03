package com.app.just_money.my_wallet.leaderborard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.databinding.ItemLeaderBoardBinding
import com.app.just_money.my_wallet.leaderborard.model.Leadership

class LeaderBoardAdapter(private val context: Context?, private val data: List<Leadership>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LBViewHolder(LayoutInflater.from(context).inflate(R.layout.item_leader_board, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as LBViewHolder
        val provider = data[position]
        if (holder.mBinding != null && context != null) {
            holder.mBinding.tvName.text = DefaultHelper.decrypt(provider.firstname)
            holder.mBinding.tvRank.text = DefaultHelper.decrypt(provider.rank)
            holder.mBinding.tvCoins.text = DefaultHelper.decrypt(provider.total_amount)

            DefaultHelper.loadImage(context, DefaultHelper.decrypt(provider.profile_pic),
                holder.mBinding.ivProfile, ContextCompat.getDrawable(context, R.drawable.ic_user_place_holder),
                ContextCompat.getDrawable(context, R.drawable.ic_user_place_holder))

            holder.mBinding.ivRankContainer.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.rank_bg_golden))
            when (holder.mBinding.tvRank.text.toString()) {
                "1" -> holder.mBinding.ivCrown.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.crown_1))
                "2" -> holder.mBinding.ivCrown.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.crown_2))
                "3" -> holder.mBinding.ivCrown.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.crown_3))
                else -> {
                    holder.mBinding.ivRankContainer.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.rank_bg_silver))
                    holder.mBinding.ivCrown.setImageDrawable(null)
                }
            }
        }
    }

    override fun getItemCount(): Int = data.size
}


class LBViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val mBinding: ItemLeaderBoardBinding? = DataBindingUtil.bind(itemView)
}


