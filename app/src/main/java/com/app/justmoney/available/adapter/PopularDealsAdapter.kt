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
import com.bumptech.glide.Glide

class PopularDealsAdapter(
    private val context: FragmentActivity,
    private val popularList: List<String>,
) : RecyclerView.Adapter<PopularDealsAdapter.ViewHolder>() {
    // holds this device's screen width,
    private var screenWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        /*  val layoutInflater = LayoutInflater.from(parent.context)
          val cellForRow = layoutInflater.inflate(R.layout.row_item_popular_deal, parent, false)
          return ViewHolder(cellForRow)*/
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_item_popular_deal, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 5//popularList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemWidth = screenWidth / 1.5

        val lp = holder.mBinding?.clBestDeal?.layoutParams
        lp!!.height = lp.height
        lp.width = itemWidth.toInt()
        holder.mBinding.clBestDeal.layoutParams = lp

        if (position == 1) {
            val imageUrl =
                "https://media1.tenor.com/images/16126ff481c2d349b972d26816915964/tenor.gif?itemid=15268410"
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.mBinding.ivLogo)
        }

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
        val mBinding: RowItemPopularDealBinding? = DataBindingUtil.bind(itemView)
    }
}
/*(
    private val list: List<Int>,
    private val context: Context
) : PagerAdapter() {
    override fun getCount(): Int {
        return 5//list.size
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mView =
            LayoutInflater.from(context).inflate(R.layout.row_item_best_deal, container, false)
        container.addView(mView, 0)
        //val txtTitle = mView.findViewById<TextView>(R.id.txtTitle)

        return mView
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    interface OnClickHandler {
        fun onClickNext(position: Int)
        fun onClickSkip()
    }

}
*/