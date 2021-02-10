package com.app.just_money.offer_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import kotlinx.android.synthetic.main.item_step.view.*


class StepsAdapter(private val context: Context?, private val list: List<String>) :
    RecyclerView.Adapter<StepsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_step, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val provider = list[position]
        holder.itemView.tvStep.text = DefaultHelper.decrypt(provider)
        holder.itemView.tvStepCount.text = "Step ${position + 1} -"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}