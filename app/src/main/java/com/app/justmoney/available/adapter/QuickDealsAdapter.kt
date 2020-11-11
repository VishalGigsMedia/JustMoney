package com.app.justmoney.available.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.justmoney.R
import com.app.justmoney.databinding.RowItemQuickDealsBinding


class QuickDealsAdapter(
    private val context: FragmentActivity,
    private val blogList: List<String>,
) : RecyclerView.Adapter<QuickDealsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_item_quick_deals, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 10//blogList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* val eachListData = blogList[position]
         holder.mBinding?.data = eachListData
         //holder.mBinding?.handler = OnClickHandler(context)
         val title = holder.mBinding?.data?.title.toString()
         val banner = holder.mBinding?.data?.banner.toString()
         val description = holder.mBinding?.data?.description
         holder.mBinding?.txtTitle?.text = title
         if (banner.isNotEmpty()) {
             Glide.with(context)
                 .load(banner)
                 .diskCacheStrategy(DiskCacheStrategy.NONE)
                 .skipMemoryCache(true)
                 .into(holder.mBinding?.ivBlog!!)
         }*/

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
}