package com.example.royal_decor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R


class PainterListAdapter(
    val painterlist: ArrayList<Painters>,
    private val listener: (Painters) -> Unit
) : RecyclerView.Adapter<PainterListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PainterListAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custpainterlistcardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PainterListAdapter.ViewHolder, position: Int) {
        holder.bindItems(painterlist[position])
        holder.itemView.setOnClickListener { listener(painterlist[position]) }
    }

    override fun getItemCount(): Int {
        return painterlist.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val custID = itemView.findViewById(R.id.cus_id) as TextView
        val custname = itemView.findViewById(R.id.cus_name) as TextView
        val custaddress = itemView.findViewById(R.id.cus_address) as TextView
        val custmobile = itemView.findViewById(R.id.cus_mobile) as TextView
        val edit = itemView.findViewById(R.id.imginfo) as ImageView
        val delete = itemView.findViewById(R.id.imgdelete) as ImageView

        fun bindItems(painterObj: Painters) {

            edit.setImageResource(0)
            edit.setBackgroundResource(R.drawable.ic_edit)
            custID.text = painterObj.id
            custname.text = painterObj.name
            custaddress.text = painterObj.address
            custmobile.text = painterObj.mobile
        }
    }
}