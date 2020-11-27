package com.app.just_money.in_progress.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.just_money.R

class InProgressAdapter(
    private val context: FragmentActivity,
    private val popularList: List<String>,
) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var adapterDataList: List<Any> = emptyList()

    companion object {
        private const val first = 0
        private const val second = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            first -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.row_item_in_progress, parent, false)
                TypeSecondViewHolder(view)
            }
            second -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.row_item_in_progress_type_second, parent, false)
                TypeFirstViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return 4//popularList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        //val element = popularList[position]
        when (holder) {
            /*is FamilyViewHolder -> holder.bind(element)
            is FriendViewHolder -> holder.bind(element)*/
            //is FriendViewHolder -> holder.bind(element as TestDataModel)
            //else -> throw IllegalArgumentException()
        }

    }

    override fun getItemViewType(position: Int): Int {
        //val comparable = data[position]
        return if (position % 2 == 0) {
            0
        } else {
            1
        }
    }


    inner class TypeSecondViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        override fun bind(item: String) {
            //Do your view assignment here from the data model
        }
    }

    inner class TypeFirstViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        override fun bind(item: String) {
            //Do your view assignment here from the data model
        }
    }
}

/*
(
    private val context: FragmentActivity,
    private val popularList: List<String>,
) : RecyclerView.Adapter<InProgressAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_item_in_progress, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 2//popularList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: RowItemInProgressBinding? = DataBindingUtil.bind(itemView)
    }
}
*/