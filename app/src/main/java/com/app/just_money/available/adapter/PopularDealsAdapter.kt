package com.app.just_money.available.adapter

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.available.model.AvailableOffer
import com.app.just_money.common_helper.BundleHelper
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultHelper.decrypt
import com.app.just_money.databinding.RowItemPopularDealBinding
import com.app.just_money.databinding.RowItemPopularDealsTypeSecondBinding
import com.app.just_money.offer_details.OfferDetailsFragment
import com.bumptech.glide.Glide

class PopularDealsAdapter(private val context: FragmentActivity?,
    private val availableOfferList: List<AvailableOffer>, private val onClicked: OnClickedPopularDeals?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var screenWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val displayMetrics = DisplayMetrics()
        context?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        return if (viewType == 0) {
            PopularDealsViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_item_popular_deal, parent, false))
        } else {
            PopularDealsWhiteViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_item_popular_deals_type_second, parent, false))
        }
    }

    override fun getItemCount(): Int = availableOfferList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is PopularDealsViewHolder) {
            val eachListData = availableOfferList[position]
            holder.mBindingPopularDeals?.data = eachListData

            val offerId = availableOfferList[position].id.toString()
            val offer_trackier_id = availableOfferList[position].offer_trackier_id.toString()
            val title = availableOfferList[position].name.toString()
            val description = availableOfferList[position].shortDescription.toString()
            val imageUrl = availableOfferList[position].image.toString()
            val actualCoins = availableOfferList[position].actualCoins.toString()
            val offerCoins = availableOfferList[position].offerCoins.toString()
            val url = decrypt(availableOfferList[position].url.toString())

            if (title.isNotEmpty()) {
                holder.mBindingPopularDeals?.txtTitle?.text = DefaultHelper.decrypt(title)
            }
            if (description.isNotEmpty()) {
                holder.mBindingPopularDeals?.txtDescription?.text = DefaultHelper.decrypt(description)
            }

            /* val imageUrl = "https://media1.tenor.com/images/16126ff481c2d349b972d26816915964/tenor.gif?itemid=15268410"*/
            if (imageUrl.isNotEmpty()) {
                if (context != null) {
                    Glide.with(context).load(DefaultHelper.decrypt(imageUrl)).placeholder(R.drawable.ic_logo).error(R.drawable.ic_logo).into(holder.mBindingPopularDeals?.ivLogo!!)
                }
                // holder.mBindingPopularDeals?.ivLogo!!.load(imageUrl)
            }


            if (actualCoins.isNotEmpty()) {
                holder.mBindingPopularDeals?.txtDealActualAmount?.text = DefaultHelper.decrypt(actualCoins)
            }
            if (offerCoins.isNotEmpty()) {
                holder.mBindingPopularDeals?.txtDealOfferAmount?.text = DefaultHelper.decrypt(offerCoins)
                holder.mBindingPopularDeals?.txtRedeemOfferAmount?.text = DefaultHelper.decrypt(offerCoins)
            }

            holder.mBindingPopularDeals?.clBestDeal?.setOnClickListener {
                val offerDetails = OfferDetailsFragment()
                val bundle = Bundle()
                bundle.putString(BundleHelper.offerId, offerId)
                bundle.putString(BundleHelper.offer_trackier_id, offer_trackier_id)
                offerDetails.arguments = bundle
                context?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, offerDetails)
                    ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
            }

            holder.mBindingPopularDeals?.clEarn?.setOnClickListener {
                onClicked?.claimOffers(offerId, url)
            }

            val itemWidth = screenWidth / 1.5
            val lp = holder.mBindingPopularDeals?.clBestDeal?.layoutParams
            lp?.height = lp?.height
            lp?.width = itemWidth.toInt()
            holder.mBindingPopularDeals?.clBestDeal?.layoutParams = lp

        } else if (holder is PopularDealsWhiteViewHolder) {
            val eachListData = availableOfferList[position]
            holder.mBindingPopularDealsSecond?.data = eachListData

            val offerId = availableOfferList[position].id.toString()
            val offer_trackier_id = availableOfferList[position].offer_trackier_id.toString()
            val title = availableOfferList[position].name.toString()
            val description = availableOfferList[position].shortDescription.toString()
            val imageUrl = availableOfferList[position].image.toString()
            val actualCoins = availableOfferList[position].actualCoins.toString()
            val offerCoins = availableOfferList[position].offerCoins.toString()

            if (title.isNotEmpty()) {
                holder.mBindingPopularDealsSecond?.txtTitle?.text = DefaultHelper.decrypt(title)
            }
            if (description.isNotEmpty()) {
                holder.mBindingPopularDealsSecond?.txtDescription?.text = DefaultHelper.decrypt(description)
            }

            if (imageUrl.isNotEmpty()) {
                if (context != null) {
                    Glide.with(context).load(DefaultHelper.decrypt(imageUrl)).placeholder(R.drawable.ic_logo).error(R.drawable.ic_logo).into(holder.mBindingPopularDealsSecond?.ivLogo!!)
                }
            }

            if (actualCoins.isNotEmpty()) {
                holder.mBindingPopularDealsSecond?.txtDealActualAmount?.text = DefaultHelper.decrypt(actualCoins)
            }
            if (offerCoins.isNotEmpty()) {
                holder.mBindingPopularDealsSecond?.txtDealOfferAmount?.text = DefaultHelper.decrypt(offerCoins)
                holder.mBindingPopularDealsSecond?.txtRedeemOfferAmount?.text = DefaultHelper.decrypt(offerCoins)
            }

            holder.mBindingPopularDealsSecond?.clBestDeal?.setOnClickListener {
                val offerDetails = OfferDetailsFragment()
                val bundle = Bundle()
                bundle.putString(BundleHelper.offerId, offerId)
                bundle.putString(BundleHelper.offer_trackier_id, offer_trackier_id)
                offerDetails.arguments = bundle
                context?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, offerDetails)
                    ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
            }

            val itemWidth = screenWidth / 1.5
            val lp = holder.mBindingPopularDealsSecond?.clBestDeal?.layoutParams
            lp!!.height = lp.height
            lp.width = itemWidth.toInt()
            holder.mBindingPopularDealsSecond.clBestDeal.layoutParams = lp
        }
    }

    interface OnClickedPopularDeals {
        fun claimOffers(offerId: String, url: String)
        fun showOfferDetails(offerId: String,offer_trackier_id:String)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = if (position % 2 == 0) 0 else 1

    class PopularDealsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingPopularDeals: RowItemPopularDealBinding? = DataBindingUtil.bind(itemView)
    }

    class PopularDealsWhiteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingPopularDealsSecond: RowItemPopularDealsTypeSecondBinding? = DataBindingUtil.bind(itemView)
    }

}