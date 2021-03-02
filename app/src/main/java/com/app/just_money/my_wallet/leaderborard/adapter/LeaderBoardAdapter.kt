package com.app.just_money.my_wallet.leaderborard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.databinding.ItemLeaderBoardBinding
import com.app.just_money.my_wallet.completed.model.CompletedList

class LeaderBoardAdapter(
    private val context: Context?,
    private val completedOfferData: List<CompletedList>?,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CompletedPinkViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_leader_board, parent, false))

    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is CompletedPinkViewHolder) {
            val eachListData = completedOfferData?.get(position)
            holder.mBinding?.tvRank?.text = "${position+1}"

            /*holder.mBindingCompletedPinkBgBinding?.clBestDeal?.setOnClickListener {
                val offerDetails = OfferDetailsFragment()
                val bundle = Bundle()
                bundle.putString(BundleHelper.offerId, offerId)
                bundle.putString(BundleHelper.displayId, offerId)
                offerDetails.arguments = bundle
                context.supportFragmentManager.beginTransaction().replace(R.id.flMain, offerDetails)
                    .addToBackStack(MainActivity::class.java.simpleName).commit()
            }*/
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()


    class CompletedPinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemLeaderBoardBinding? = DataBindingUtil.bind(itemView)
    }


}