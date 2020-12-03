package com.app.just_money.my_wallet.completed.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.BundleHelper
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.databinding.RowItemCompletedPinkBgBinding
import com.app.just_money.databinding.RowItemCompletedWhiteBgBinding
import com.app.just_money.in_progress.adapter.InProgressAdapter
import com.app.just_money.my_wallet.completed.model.CompletedList
import com.app.just_money.offer_details.OfferDetailsFragment
import com.bumptech.glide.Glide

class CompletedAdapter(
    private val context: FragmentActivity,
    private val completedOfferData: List<CompletedList>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val first = 0
        private const val second = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            return CompletedPinkViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.row_item_completed_pink_bg,
                    parent,
                    false
                )
            )
        } else {
            return CompletedWhiteViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.row_item_completed_white_bg,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return completedOfferData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is CompletedPinkViewHolder) {
            val eachListData = completedOfferData[position]
            holder.mBindingCompletedPinkBgBinding?.data = eachListData

            val offerId = completedOfferData[position].id.toString()
            val title = completedOfferData[position].name.toString()
            val description = completedOfferData[position].shortDescription.toString()
            val imageUrl = completedOfferData[position].image.toString()
            val actualCoins = completedOfferData[position].actualCoins.toString()
            val offerCoins = completedOfferData[position].offerCoins.toString()

            if (title.isNotEmpty()) {
                holder.mBindingCompletedPinkBgBinding?.txtTitle?.text = DefaultHelper.decrypt(title)
            }
            if (description.isNotEmpty()) {
                holder.mBindingCompletedPinkBgBinding?.txtDescription?.text =
                    DefaultHelper.decrypt(description)
            }

            if (imageUrl.isNotEmpty()) {
                Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(holder.mBindingCompletedPinkBgBinding?.ivLogo!!)
            }

            if (actualCoins.isNotEmpty()) {
                holder.mBindingCompletedPinkBgBinding?.txtDealActualAmount?.text =
                    DefaultHelper.decrypt(actualCoins)
            }
            if (offerCoins.isNotEmpty()) {
                holder.mBindingCompletedPinkBgBinding?.txtDealOfferAmount?.text =
                    DefaultHelper.decrypt(offerCoins)
            }

            holder.mBindingCompletedPinkBgBinding?.clBestDeal?.setOnClickListener {
                val offerDetails = OfferDetailsFragment()
                val bundle = Bundle()
                bundle.putString(BundleHelper.offerId, offerId)
                bundle.putString(BundleHelper.displayId, offerId)
                offerDetails.arguments = bundle
                context.supportFragmentManager.beginTransaction()
                    .replace(R.id.flMain, offerDetails)
                    .addToBackStack(MainActivity::class.java.simpleName)
                    .commit()
            }

        } else if (holder is CompletedWhiteViewHolder) {
            val eachListData = completedOfferData[position]
            holder.mBindingCompletedWhiteBgBinding?.data = eachListData

            val offerId = completedOfferData[position].id.toString()
            val title = completedOfferData[position].name.toString()
            val description = completedOfferData[position].shortDescription.toString()
            val imageUrl = completedOfferData[position].image.toString()
            val actualCoins = completedOfferData[position].actualCoins.toString()
            val offerCoins = completedOfferData[position].offerCoins.toString()

            if (title.isNotEmpty()) {
                holder.mBindingCompletedWhiteBgBinding?.txtTitle?.text = DefaultHelper.decrypt(title)
            }
            if (description.isNotEmpty()) {
                holder.mBindingCompletedWhiteBgBinding?.txtDescription?.text =
                    DefaultHelper.decrypt(description)
            }

            if (imageUrl.isNotEmpty()) {
                Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(holder.mBindingCompletedWhiteBgBinding?.ivLogo!!)
            }

            if (actualCoins.isNotEmpty()) {
                holder.mBindingCompletedWhiteBgBinding?.txtDealActualAmount?.text =
                    DefaultHelper.decrypt(actualCoins)
            }
            if (offerCoins.isNotEmpty()) {
                holder.mBindingCompletedWhiteBgBinding?.txtDealOfferAmount?.text =
                    DefaultHelper.decrypt(offerCoins)
            }

            holder.mBindingCompletedWhiteBgBinding?.clBestDeal?.setOnClickListener {
                val offerDetails = OfferDetailsFragment()
                val bundle = Bundle()
                bundle.putString(BundleHelper.offerId, offerId)
                bundle.putString(BundleHelper.displayId, offerId)
                offerDetails.arguments = bundle
                context.supportFragmentManager.beginTransaction()
                    .replace(R.id.flMain, offerDetails)
                    .addToBackStack(MainActivity::class.java.simpleName)
                    .commit()
            }
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

    class CompletedPinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingCompletedPinkBgBinding: RowItemCompletedPinkBgBinding? = DataBindingUtil.bind(itemView)
    }

    class CompletedWhiteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingCompletedWhiteBgBinding: RowItemCompletedWhiteBgBinding? =
            DataBindingUtil.bind(itemView)
    }

}