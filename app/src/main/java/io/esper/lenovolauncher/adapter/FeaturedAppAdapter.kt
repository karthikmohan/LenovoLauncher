package io.esper.lenovolauncher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import de.hdodenhof.circleimageview.CircleImageView
import io.esper.lenovolauncher.R
import io.esper.lenovolauncher.model.FeaturedAppItem
import io.esper.lenovolauncher.utils.GeneralUtils.openApp


class FeaturedAppAdapter(
    private val context: Context,
    private val featuredAppItem: MutableList<FeaturedAppItem>?,
) :
    RecyclerView.Adapter<FeaturedAppAdapter.MyViewHolder>() {

    private var mContext: Context? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        mContext = context
        val view: View = inflater.inflate(R.layout.featured_apps_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int,
    ) {

        holder.name.text = featuredAppItem!![position].appName

        if (!featuredAppItem[position].appIcon.isNullOrEmpty()) {
            Glide.with(mContext!!).load(featuredAppItem[position].appIcon)
                .placeholder(R.mipmap.ic_launcher)
                .priority(Priority.HIGH).into(holder.image)
        }

        holder.appButton.setOnClickListener {
            featuredAppItem[position].packageName?.let { it1 -> openApp(context, it1) }
        }
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.approved_apps_text) as TextView
        var image = itemView.findViewById(R.id.approved_apps_image) as CircleImageView
        var appButton = itemView.findViewById(R.id.app_button) as LinearLayout
    }

    override fun getItemCount(): Int {
        return featuredAppItem?.size ?: 0
    }
}