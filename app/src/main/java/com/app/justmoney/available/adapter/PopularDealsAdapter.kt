package com.app.justmoney.available.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.justmoney.R
import com.app.justmoney.databinding.RowItemPopularDealBinding
import com.app.justmoney.databinding.RowItemPopularDealsTypeSecondBinding

class PopularDealsAdapter(
    private val context: FragmentActivity,
    private val popularList: List<String>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    // holds this device's screen width,
    private var screenWidth = 0

    companion object {
        private const val first = 0
        private const val second = 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        if (viewType == 0) {
            return TypeFirstViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.row_item_popular_deal,
                    parent,
                    false
                )
            )
        } else {
            return TypeSecondViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.row_item_popular_deals_type_second,
                    parent,
                    false
                )
            )
        }
    }


    override fun getItemCount(): Int {
        return 5//popularList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is TypeFirstViewHolder) {
            /*val eachListData = popularList[position]
            holder.mBinding?.data = eachListData*/

            val itemWidth = screenWidth / 1.5
            val lp = holder.mBindingPopularDeals?.clBestDeal?.layoutParams
            lp!!.height = lp.height
            lp.width = itemWidth.toInt()
            holder.mBindingPopularDeals.clBestDeal.layoutParams = lp

        } else if (holder is TypeSecondViewHolder) {
            val itemWidth = screenWidth / 1.5
            val lp = holder.mBindingPopularDealsSecond?.clBestDeal?.layoutParams
            lp!!.height = lp.height
            lp.width = itemWidth.toInt()
            holder.mBindingPopularDealsSecond.clBestDeal.layoutParams = lp
        }

        /*if (position == 1) {
            val imageUrl =
                "https://media1.tenor.com/images/16126ff481c2d349b972d26816915964/tenor.gif?itemid=15268410"
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.mBinding.ivLogo)
        }*/
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            0
        } else {
            1
        }
    }

    /* class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val mBinding: RowItemPopularDealBinding? = DataBindingUtil.bind(itemView)
     }*/

    /*class TypeFirstViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingPopularDeals: RowItemPopularDealBinding? = DataBindingUtil.bind(itemView)
    }

    class TypeSecondViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingPopularDealsSecond: RowItemPopularDealsTypeSecondBinding? =
            DataBindingUtil.bind(itemView)
    }
*/
    class TypeFirstViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingPopularDeals: RowItemPopularDealBinding? =
            DataBindingUtil.bind(itemView)
    }

    class TypeSecondViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBindingPopularDealsSecond: RowItemPopularDealsTypeSecondBinding? =
            DataBindingUtil.bind(itemView)
    }


}