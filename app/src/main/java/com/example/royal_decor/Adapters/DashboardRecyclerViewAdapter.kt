package com.example.royal_decor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Models.DashboardRVObj
import com.example.royal_decor.R


class DashboardRecyclerViewAdapter(
    val userList: ArrayList<DashboardRVObj>,
    private val listener: (DashboardRVObj) -> Unit
) : RecyclerView.Adapter<DashboardRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DashboardRecyclerViewAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclercardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: DashboardRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
        holder.itemView.setOnClickListener { listener(userList[position]) }
    }

    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cdtext = itemView.findViewById(R.id.cd_text) as TextView
        val cdimage = itemView.findViewById(R.id.cd_icon) as ImageView

        fun bindItems(detailsobj: DashboardRVObj) {
            cdtext.text = detailsobj.text
            cdimage.setBackgroundResource(detailsobj.icon_id)
        }
    }
}