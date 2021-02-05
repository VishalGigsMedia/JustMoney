package com.app.just_money.available.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.available.model.AvailableOffer
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.databinding.RowItemQuickDealsBinding
import com.bumptech.glide.Glide


class QuickDealsAdapter(
    private val context: FragmentActivity,
    private val quickDealsList: List<AvailableOffer>,
    private val onClickedQuickDeals: OnClickedQuickDeals
) : RecyclerView.Adapter<QuickDealsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_item_quick_deals, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return quickDealsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val eachListData = quickDealsList[position]
        holder.mBinding?.data = eachListData

        val id = DefaultHelper.decrypt(quickDealsList[position].id.toString())
        val title = quickDealsList[position].name.toString()
        val imageUrl = quickDealsList[position].image.toString()
        val buttonText = quickDealsList[position].buttonText.toString()
        val url = DefaultHelper.decrypt(quickDealsList[position].buttonText.toString())

        if (title.isNotEmpty()) {
            holder.mBinding?.txtTitle?.text = DefaultHelper.decrypt(title)
        }
        if (buttonText.isNotEmpty()) {
            holder.mBinding?.txtTakeActionMessage?.text = DefaultHelper.decrypt(buttonText)
        }

        if (imageUrl.isNotEmpty()) {
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.mBinding?.ivLogo!!)
        }
        holder.mBinding?.txtTakeActionMessage?.setOnClickListener {
            onClickedQuickDeals.getOffers(id,url)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: RowItemQuickDealsBinding? = DataBindingUtil.bind(itemView)
    }

    interface OnClickedQuickDeals {
        fun getOffers(appId: String, url: String)
    }
}