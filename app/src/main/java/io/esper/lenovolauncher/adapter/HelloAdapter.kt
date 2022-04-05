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
import io.esper.lenovolauncher.model.HospitalDbItem


class HelloAdapter(private val context: Context, private val hospitalDbItem: MutableList<HospitalDbItem>) :
    RecyclerView.Adapter<HelloAdapter.MyViewHolder>() {

    private var mContext: Context? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        mContext = context
        val view: View = inflater.inflate(R.layout.user_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        holder.name.text = hospitalDbItem[position].patientName

        if (!hospitalDbItem[position].patientImage.isNullOrEmpty()) {
            Glide.with(mContext!!).load(hospitalDbItem[position].patientImage)
                .placeholder(R.drawable.default_care_team_image)
                .priority(Priority.HIGH).into(holder.image)
        }
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById(R.id.name) as TextView
        var image = itemView.findViewById(R.id.image) as CircleImageView
    }

    override fun getItemCount(): Int {
        return hospitalDbItem.size
    }
}