package com.app.just_money.available.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.available.model.AvailableOffer
import com.app.just_money.common_helper.DefaultHelper.decrypt
import com.app.just_money.databinding.RowItemQuickDealsBinding
import com.bumptech.glide.Glide


class  QuickDealsAdapter(private val context: FragmentActivity?, private val quickDealsList: List<AvailableOffer>,
    private val onClickedQuickDeals: OnClickedQuickDeals?) : RecyclerView.Adapter<QuickDealsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_item_quick_deals, parent, false))
    }

    override fun getItemCount(): Int {
        return quickDealsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val eachListData = quickDealsList[position]
        holder.mBinding?.data = eachListData

        val id = quickDealsList[position].id.toString()
        val title = decrypt(quickDealsList[position].name.toString())
        val imageUrl = decrypt(quickDealsList[position].image.toString())
        val buttonText = context?.getString(R.string.button_text_q_d)
        val url = decrypt(quickDealsList[position].url.toString())

        if (title.isNotEmpty()) {
            holder.mBinding?.txtTitle?.text = title
        }
        if (buttonText != null) {
            if (buttonText.isNotEmpty()) {
                holder.mBinding?.txtTakeActionMessage?.text = buttonText
            }
        }

        if (imageUrl.isNotEmpty() && context != null) {
            Glide.with(context).load(imageUrl).placeholder(R.drawable.logo_without_text).error(R.drawable.logo_without_text).into(holder.mBinding?.ivLogo!!)
        }
        holder.mBinding?.txtTakeActionMessage?.setOnClickListener {
            onClickedQuickDeals?.getOffers(id, url)
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
        fun getOffers(offer_id: String, url: String)
    }
}