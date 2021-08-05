package com.app.cent4free.in_progress.adapter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.cent4free.MainActivity
import com.app.cent4free.R
import com.app.cent4free.common_helper.BundleHelper
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.databinding.RowItemInProgressBinding
import com.app.cent4free.databinding.RowItemInProgressTypeSecondBinding
import com.app.cent4free.in_progress.model.PendingList
import com.app.cent4free.my_wallet.faq.FaqFragment
import com.app.cent4free.offer_details.OfferDetailsFragment
import com.bumptech.glide.Glide

class InProgressAdapter(private val context: FragmentActivity?, private val inProgressList: List<PendingList>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            InProgressViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_item_in_progress, parent, false))
        } else {
            InProgressSecondViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_item_in_progress_type_second, parent, false))
        }
    }

    override fun getItemCount() = inProgressList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is InProgressViewHolder) {
            val eachListData = inProgressList[position]
            holder.mBindingInProgressBinding?.data = eachListData

            val offerId = inProgressList[position].id.toString()
            val offer_trackier_id = inProgressList[position].offer_trackier_id.toString()
            val title = inProgressList[position].name.toString()
            val description = inProgressList[position].shortDescription.toString()
            val imageUrl = inProgressList[position].image.toString()
            val created_at = inProgressList[position].created_at.toString()
            val offerCoins = inProgressList[position].offerCoins.toString()
            val url = decrypt(inProgressList[position].url.toString())

            if (title.isNotEmpty()) {
                holder.mBindingInProgressBinding?.txtTitle?.text = decrypt(title)
            }
            if (description.isNotEmpty()) {
                holder.mBindingInProgressBinding?.txtDescription?.text = decrypt(description)
            }

            if (imageUrl.isNotEmpty()) {
                if (context != null) {
                    Glide.with(context).load(decrypt(imageUrl)).placeholder(R.drawable.logo_without_text).error(R.drawable.logo_without_text)
                        .into(holder.mBindingInProgressBinding?.ivLogo!!)
                }
            }

            if (created_at.isNotEmpty()) {
                holder.mBindingInProgressBinding?.txtCreatedAt?.text = decrypt(created_at)
            }
            if (offerCoins.isNotEmpty()) {
                holder.mBindingInProgressBinding?.txtDealOfferAmount?.text = decrypt(offerCoins)
                holder.mBindingInProgressBinding?.txtRedeemOfferAmount?.text = decrypt(offerCoins)
            }

            holder.mBindingInProgressBinding?.clBestDeal?.setOnClickListener {
                val offerDetails = OfferDetailsFragment()
                val bundle = Bundle()
                bundle.putString(BundleHelper.offerId, offerId)
                bundle.putString(BundleHelper.offer_trackier_id, offer_trackier_id)
                bundle.putString(BundleHelper.source, BundleHelper.inProgress)
                offerDetails.arguments = bundle
                context?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, offerDetails)
                    ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
            }

            holder.mBindingInProgressBinding?.clEarn?.setOnClickListener {
                if (url.contains("http")) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    context?.startActivity(intent)
                }
            }
            holder.mBindingInProgressBinding?.txtHaveAQuestion?.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.flMain, FaqFragment()).addToBackStack(MainActivity::class.java.simpleName)
                    .commit()
            }

        } else if (holder is InProgressSecondViewHolder) {
            val eachListData = inProgressList[position]
            holder.mBindingInProgressTypeSecondBinding?.data = eachListData

            val offerId = inProgressList[position].id.toString()
            val offer_trackier_id = inProgressList[position].offer_trackier_id.toString()
            val title = inProgressList[position].name.toString()
            val description = inProgressList[position].shortDescription.toString()
            val imageUrl = inProgressList[position].image.toString()
            val created_at = inProgressList[position].created_at.toString()
            val offerCoins = inProgressList[position].offerCoins.toString()
            val url = decrypt(inProgressList[position].url.toString())

            if (title.isNotEmpty()) {
                holder.mBindingInProgressTypeSecondBinding?.txtTitle?.text = decrypt(title)
            }
            if (description.isNotEmpty()) {
                holder.mBindingInProgressTypeSecondBinding?.txtDescription?.text = decrypt(description)
            }

            /* val imageUrl = "https://media1.tenor.com/images/16126ff481c2d349b972d26816915964/tenor.gif?itemid=15268410"*/
            if (imageUrl.isNotEmpty()) {
                if (context != null) {
                    Glide.with(context).load(decrypt(imageUrl)).placeholder(R.drawable.logo_without_text).error(R.drawable.logo_without_text)
                        .into(holder.mBindingInProgressTypeSecondBinding?.ivLogo!!)
                }
            }

            if (created_at.isNotEmpty()) {
                holder.mBindingInProgressTypeSecondBinding?.txtCreatedAt?.text = decrypt(created_at)
            }
            if (offerCoins.isNotEmpty()) {
                holder.mBindingInProgressTypeSecondBinding?.txtDealOfferAmount?.text = decrypt(offerCoins)
                holder.mBindingInProgressTypeSecondBinding?.txtRedeemOfferAmount?.text = decrypt(offerCoins)
            }

            holder.mBindingInProgressTypeSecondBinding?.clBestDeal?.setOnClickListener {
                val offerDetails = OfferDetailsFragment()
                val bundle = Bundle()
                bundle.putString(BundleHelper.offerId, offerId)
                bundle.putString(BundleHelper.offer_trackier_id, offer_trackier_id)
                bundle.putString(BundleHelper.source, BundleHelper.inProgress)
                offerDetails.arguments = bundle
                context?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, offerDetails)
                    ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
            }

            holder.mBindingInProgressTypeSecondBinding?.clEarn?.setOnClickListener {
                if (url.contains("http")) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    context?.startActivity(intent)
                }
            }

            holder.mBindingInProgressTypeSecondBinding?.txtHaveAQuestion?.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.flMain, FaqFragment()).addToBackStack(MainActivity::class.java.simpleName)
                    .commit()
            }

        }
    }

    override fun getItemViewType(position: Int): Int = if (position % 2 == 0) 0 else 1

    class InProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingInProgressBinding: RowItemInProgressBinding? = DataBindingUtil.bind(itemView)
    }

    class InProgressSecondViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingInProgressTypeSecondBinding: RowItemInProgressTypeSecondBinding? =
            DataBindingUtil.bind(itemView)
    }

}
