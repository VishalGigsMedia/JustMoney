package com.app.cent4free.my_wallet.faq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper
import com.app.cent4free.databinding.RowItemFaqBinding
import com.app.cent4free.my_wallet.faq.model.FaqData

class FaqAdapter(private val context: FragmentActivity, private val faqList: List<FaqData>) :
    RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_item_faq, parent, false))
    }

    override fun getItemCount(): Int = faqList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = faqList[position]
        holder.mBinding?.data = eachListData

        val question = DefaultHelper.decrypt(eachListData.faqQuestion.toString())
        val answer = DefaultHelper.decrypt(eachListData.faqAnswer.toString())
        holder.mBinding?.txtQuestion?.text = question
        holder.mBinding?.txtAnswer?.text = answer


        holder.mBinding?.clQuestion?.setOnClickListener {
            println("eachListData: " + eachListData.showMore.toString())
            when {
                !eachListData.showMore -> {
                    eachListData.showMore = true
                    notifyDataSetChanged()

                    holder.mBinding.ivShowMore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_show_less))
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(holder.mBinding.ivShowMore.drawable),
                        ContextCompat.getColor(context, R.color.gray_800)
                    )
                    holder.mBinding.txtQuestion.setTextColor(ContextCompat.getColor(context, R.color.black))
                    holder.mBinding.txtAnswer.visibility = VISIBLE
                    holder.mBinding.ivShowMore.tag = "less"
                }
                eachListData.showMore -> {
                    eachListData.showMore = false
                    notifyDataSetChanged()

                    holder.mBinding.ivShowMore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_show_more))
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(holder.mBinding.ivShowMore.drawable),
                        ContextCompat.getColor(context, R.color.gray_800)
                    )
                    holder.mBinding.txtQuestion.setTextColor(ContextCompat.getColor(context, R.color.gray_800))
                    holder.mBinding.txtAnswer.visibility = GONE
                    holder.mBinding.ivShowMore.tag = "more"
                }
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