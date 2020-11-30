package com.app.just_money.my_wallet.faq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.databinding.RowItemFaqBinding
import com.app.just_money.my_wallet.faq.model.FaqData

class FaqAdapter(
    private val context: FragmentActivity,
    private val faqList: List<FaqData>,
) : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_item_faq, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var eachListData = faqList[position]
        holder.mBinding?.data = eachListData

        val question = DefaultHelper.decrypt(eachListData.faqQuestion.toString())
        val answer = DefaultHelper.decrypt(eachListData.faqAnswer.toString())
        holder.mBinding?.txtQuestion?.text = question
        holder.mBinding?.txtAnswer?.text = answer

        holder.mBinding?.ivShowMore?.setOnClickListener {

            if (!eachListData.showMore) {
                eachListData.showMore = true
                notifyDataSetChanged()

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
            } else if (eachListData.showMore) {
                eachListData.showMore = false
                notifyDataSetChanged()

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
            println("eachListData: " + eachListData.showMore.toString())
            if (!eachListData.showMore) {
                eachListData.showMore = true
                notifyDataSetChanged()

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
            } else if (eachListData.showMore) {
                eachListData.showMore = false
                notifyDataSetChanged()

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