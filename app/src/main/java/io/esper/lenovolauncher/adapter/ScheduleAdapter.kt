package io.esper.lenovolauncher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.esper.lenovolauncher.R
import io.esper.lenovolauncher.model.ScheduleItem


class ScheduleAdapter(
    private val context: Context,
    private val mealInfoItem: MutableList<ScheduleItem>?,
) :
    RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>() {

    private var mContext: Context? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        mContext = context
        val view: View = inflater.inflate(R.layout.meal_info_item, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int,
    ) {
        holder.mealTiming.text = mealInfoItem!![position].dateTime
        holder.meal.text =
            mealInfoItem[position].meetingTitle + " | " + mealInfoItem[position].meetingLocation
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mealTiming: TextView = itemView.findViewById(R.id.meal_timing) as TextView
        var meal = itemView.findViewById(R.id.meal_type) as TextView
    }

    override fun getItemCount(): Int {
        return mealInfoItem?.size ?: 0
    }
}