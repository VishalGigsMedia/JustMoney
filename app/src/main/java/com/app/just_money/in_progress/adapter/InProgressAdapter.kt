package com.app.just_money.in_progress.adapter

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
import com.app.just_money.databinding.RowItemInProgressBinding
import com.app.just_money.databinding.RowItemInProgressTypeSecondBinding
import com.app.just_money.in_progress.model.PendingList
import com.app.just_money.offer_details.OfferDetailsFragment
import com.bumptech.glide.Glide

class InProgressAdapter(private val context: FragmentActivity, private val inProgressList: List<PendingList>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            InProgressViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_item_in_progress, parent, false))
        } else {
            InProgressSecondViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_item_in_progress_type_second, parent, false))
        }
    }

    override fun getItemCount() = inProgressList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is InProgressViewHolder) {
            val eachListData = inProgressList[position]
            holder.mBindingInProgressBinding?.data = eachListData

            val offerId = inProgressList[position].id.toString()
            val title = inProgressList[position].name.toString()
            val description = inProgressList[position].shortDescription.toString()
            val imageUrl = inProgressList[position].image.toString()
            val actualCoins = inProgressList[position].actualCoins.toString()
            val offerCoins = inProgressList[position].offerCoins.toString()

            if (title.isNotEmpty()) {
                holder.mBindingInProgressBinding?.txtTitle?.text = DefaultHelper.decrypt(title)
            }
            if (description.isNotEmpty()) {
                holder.mBindingInProgressBinding?.txtDescription?.text =
                    DefaultHelper.decrypt(description)
            }

            /* val imageUrl = "https://media1.tenor.com/images/16126ff481c2d349b972d26816915964/tenor.gif?itemid=15268410"*/
            if (imageUrl.isNotEmpty()) {
                Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo).into(holder.mBindingInProgressBinding?.ivLogo!!)
            }

            if (actualCoins.isNotEmpty()) {
                holder.mBindingInProgressBinding?.txtDealActualAmount?.text =
                    DefaultHelper.decrypt(actualCoins)
            }
            if (offerCoins.isNotEmpty()) {
                holder.mBindingInProgressBinding?.txtDealOfferAmount?.text =
                    DefaultHelper.decrypt(offerCoins)
                holder.mBindingInProgressBinding?.txtRedeemOfferAmount?.text =
                    DefaultHelper.decrypt(offerCoins)
            }

            holder.mBindingInProgressBinding?.clBestDeal?.setOnClickListener {
                val offerDetails = OfferDetailsFragment()
                val bundle = Bundle()
                bundle.putString(BundleHelper.offerId, offerId)
                bundle.putString(BundleHelper.displayId, offerId)
                offerDetails.arguments = bundle
                context.supportFragmentManager.beginTransaction().replace(R.id.flMain, offerDetails)
                    .addToBackStack(MainActivity::class.java.simpleName).commit()
            }

            holder.mBindingInProgressBinding?.clEarn?.setOnClickListener {
                // onClicked.claimOffers(offerId)
            }

        } else if (holder is InProgressSecondViewHolder) {
            val eachListData = inProgressList[position]
            holder.mBindingInProgressTypeSecondBinding?.data = eachListData

            val offerId = inProgressList[position].id.toString()
            val title = inProgressList[position].name.toString()
            val description = inProgressList[position].shortDescription.toString()
            val imageUrl = inProgressList[position].image.toString()
            val actualCoins = inProgressList[position].actualCoins.toString()
            val offerCoins = inProgressList[position].offerCoins.toString()

            if (title.isNotEmpty()) {
                holder.mBindingInProgressTypeSecondBinding?.txtTitle?.text =
                    DefaultHelper.decrypt(title)
            }
            if (description.isNotEmpty()) {
                holder.mBindingInProgressTypeSecondBinding?.txtDescription?.text =
                    DefaultHelper.decrypt(description)
            }

            /* val imageUrl = "https://media1.tenor.com/images/16126ff481c2d349b972d26816915964/tenor.gif?itemid=15268410"*/
            if (imageUrl.isNotEmpty()) {
                Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(holder.mBindingInProgressTypeSecondBinding?.ivLogo!!)
            }

            if (actualCoins.isNotEmpty()) {
                holder.mBindingInProgressTypeSecondBinding?.txtDealActualAmount?.text =
                    DefaultHelper.decrypt(actualCoins)
            }
            if (offerCoins.isNotEmpty()) {
                holder.mBindingInProgressTypeSecondBinding?.txtDealOfferAmount?.text =
                    DefaultHelper.decrypt(offerCoins)
                holder.mBindingInProgressTypeSecondBinding?.txtRedeemOfferAmount?.text =
                    DefaultHelper.decrypt(offerCoins)
            }

            holder.mBindingInProgressTypeSecondBinding?.clBestDeal?.setOnClickListener {
                val offerDetails = OfferDetailsFragment()
                val bundle = Bundle()
                bundle.putString(BundleHelper.offerId, offerId)
                bundle.putString(BundleHelper.displayId, offerId)
                offerDetails.arguments = bundle
                context.supportFragmentManager.beginTransaction().replace(R.id.flMain, offerDetails)
                    .addToBackStack(MainActivity::class.java.simpleName).commit()
            }

            holder.mBindingInProgressTypeSecondBinding?.clEarn?.setOnClickListener {
                // onClicked.claimOffers(offerId)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        //val comparable = data[position]
        return if (position % 2 == 0) {
            0
        } else {
            1
        }
    }


    class InProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingInProgressBinding: RowItemInProgressBinding? = DataBindingUtil.bind(itemView)
    }

    class InProgressSecondViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingInProgressTypeSecondBinding: RowItemInProgressTypeSecondBinding? =
            DataBindingUtil.bind(itemView)
    }

}
