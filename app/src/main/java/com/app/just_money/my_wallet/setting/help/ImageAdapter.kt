package com.app.just_money.my_wallet.setting.help

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R
import com.app.just_money.databinding.IconFeedbackBinding
import java.io.File
import java.io.IOException

class ImageAdapter(private val context: Context, private val list: List<File>, private val helpUsFragment: HelpUsFragment) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.icon_feedback, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val provider = list[position]
        holder.mBinding?.ivImage?.clipToOutline = true
        val myBitmap = BitmapFactory.decodeFile(provider.absolutePath)
        val bitmap = helpUsFragment.modifyOrientation(myBitmap, provider.absolutePath)
        holder.mBinding?.ivImage?.setImageBitmap(bitmap)

        holder.mBinding?.ivRemove?.setOnClickListener{
            helpUsFragment.removeImage(position)
        }

    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int= position

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: IconFeedbackBinding? = DataBindingUtil.bind(itemView)
    }


}