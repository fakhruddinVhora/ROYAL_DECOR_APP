package com.example.royal_decor.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R


class CustomerListAdapter(
    val custlist: ArrayList<Painters>,
    val clickListener: customerOnclickListener
) : RecyclerView.Adapter<CustomerListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerListAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custpainterlistcardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CustomerListAdapter.ViewHolder, position: Int) {
        holder.bindItems(custlist[position], clickListener)
    }

    override fun getItemCount(): Int {
        return custlist.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val custID = itemView.findViewById(R.id.cus_id) as TextView
        val custname = itemView.findViewById(R.id.cus_name) as TextView
        val custaddress = itemView.findViewById(R.id.cus_address) as TextView
        val custmobile = itemView.findViewById(R.id.cus_mobile) as TextView

        val info = itemView.findViewById(R.id.imginfo) as ImageView
        val delete = itemView.findViewById(R.id.imgdelete) as ImageView

        fun bindItems(
            custObj: Painters,
            clickListener: customerOnclickListener
        ) {
            custID.text = custObj.id
            custname.text = custObj.name
            custaddress.text = custObj.address
            custmobile.text = custObj.mobile

            info.setOnClickListener {
                clickListener.OnInfoClick(custObj)
            }
            delete.setOnClickListener {
                clickListener.OnDeleteClick(custObj)
            }

        }
    }

    interface customerOnclickListener {
        fun OnDeleteClick(item: Painters)
        fun OnInfoClick(item: Painters)
    }
}