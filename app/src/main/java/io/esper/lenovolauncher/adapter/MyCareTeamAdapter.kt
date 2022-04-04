package io.esper.lenovolauncher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import de.hdodenhof.circleimageview.CircleImageView
import io.esper.lenovolauncher.R
import io.esper.lenovolauncher.model.MyCareTeamItem


class MyCareTeamAdapter(private val context: Context, private val careTeamItem: MutableList<MyCareTeamItem>?) :
    RecyclerView.Adapter<MyCareTeamAdapter.MyViewHolder>() {

    private var mContext: Context? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        mContext = context
        val view: View = inflater.inflate(R.layout.my_care_team_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        holder.name.text = careTeamItem!![position].name
        holder.designation.text = careTeamItem[position].designation

        if (!careTeamItem[position].image.isNullOrEmpty()) {
            Glide.with(mContext!!).load(careTeamItem[position].image)
                .placeholder(R.drawable.default_care_team_image)
                .priority(Priority.HIGH).into(holder.image)
        }
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.my_care_team_name) as TextView
        var designation: TextView = itemView.findViewById(R.id.my_care_team_designation) as TextView
        var image = itemView.findViewById(R.id.my_care_team_image) as CircleImageView
    }

    override fun getItemCount(): Int {
        return careTeamItem?.size ?: 0
    }
}