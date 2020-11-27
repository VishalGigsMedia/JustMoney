package com.app.just_money.my_wallet.faq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.databinding.RowItemFaqBinding

class FaqAdapter(
    private val context: FragmentActivity,
    private val faqList: List<String>,
) : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_item_faq, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 10//faqList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mBinding?.ivShowMore?.setOnClickListener {
            if (holder.mBinding.ivShowMore.tag == "more") {
                holder.mBinding.ivShowMore.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_show_less
                    )
                )
                holder.mBinding.txtQuestion.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black
                    )
                )
                holder.mBinding.txtAnswer.visibility = View.VISIBLE
                holder.mBinding.ivShowMore.tag = "less"
            } else if (holder.mBinding.ivShowMore.tag == "less") {
                holder.mBinding.ivShowMore.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_show_more
                    )
                )
                holder.mBinding.txtQuestion.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.grey_500
                    )
                )
                holder.mBinding.txtAnswer.visibility = View.GONE
                holder.mBinding.ivShowMore.tag = "more"
            }
        }

        holder.mBinding?.txtQuestion?.setOnClickListener {
            if (holder.mBinding.ivShowMore.tag == "more") {
                holder.mBinding.ivShowMore.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_show_less
                    )
                )
                holder.mBinding.txtQuestion.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black
                    )
                )
                holder.mBinding.txtAnswer.visibility = View.VISIBLE
                holder.mBinding.ivShowMore.tag = "less"
            } else if (holder.mBinding.ivShowMore.tag == "less") {
                holder.mBinding.ivShowMore.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_show_more
                    )
                )
                holder.mBinding.txtQuestion.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.grey_500
                    )
                )
                holder.mBinding.txtAnswer.visibility = View.GONE
                holder.mBinding.ivShowMore.tag = "more"
            }
        }
        /* val eachListData = faqList[position]
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
        val mBinding: RowItemFaqBinding? = DataBindingUtil.bind(itemView)
    }
}