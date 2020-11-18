package com.app.justmoney.in_progress.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.justmoney.R

class TestAdapter(private val context: Context, private val dataSet: List<String>) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var adapterDataList: List<Any> = emptyList()

    companion object {
        private const val TYPE_FAMILY = 0
        private const val TYPE_FRIEND = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_FAMILY -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.row_item_in_progress, parent, false)
                FamilyViewHolder(view)
            }
            TYPE_FRIEND -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.row_item_in_progress_type_second, parent, false)
                FriendViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    //-----------onCreateViewHolder: bind view with data model---------
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = adapterDataList[position]
        when (holder) {
            is FamilyViewHolder -> holder.bind(element as TestDataModel)
            is FriendViewHolder -> holder.bind(element as TestDataModel)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        //val comparable = data[position]

        if (position % 2 == 0) {
            return TestAdapter.TYPE_FAMILY

        } else {
            return TestAdapter.TYPE_FAMILY
        }

        /*return when (position % 2 == 0) {
            is String -> TestAdapter.TYPE_FAMILY
            is TestDataModel -> TestAdapter.TYPE_FRIEND
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }*/
    }

    override fun getItemCount(): Int {
        return adapterDataList.size
    }
}


class FamilyViewHolder(itemView: View) : BaseViewHolder<TestDataModel>(itemView) {

    override fun bind(item: TestDataModel) {
        //Do your view assignment here from the data model
    }
}

class FriendViewHolder(itemView: View) : BaseViewHolder<TestDataModel>(itemView) {
    override fun bind(item: TestDataModel) {
        //Do your view assignment here from the data model
    }
}

