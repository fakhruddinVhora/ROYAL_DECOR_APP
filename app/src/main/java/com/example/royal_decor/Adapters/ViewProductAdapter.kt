package com.example.royal_decor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Models.Product
import com.example.royal_decor.R

class ViewProductAdapter(
    val prodList: List<Product>,
    val clicklistener: OnProductClickedListener
) : RecyclerView.Adapter<ViewProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val custID = itemView.findViewById(R.id.cus_id) as TextView
        val custname = itemView.findViewById(R.id.cus_name) as TextView
        val custaddress = itemView.findViewById(R.id.cus_address) as TextView
        val custmobile = itemView.findViewById(R.id.cus_mobile) as TextView
        val edit = itemView.findViewById(R.id.imginfo) as ImageView
        val delete = itemView.findViewById(R.id.imgdelete) as ImageView


        fun bindItems(prodObj: Product, action: ViewProductAdapter.OnProductClickedListener) {

            edit.setImageResource(0)
            edit.setBackgroundResource(R.drawable.ic_edit)
            custID.text = prodObj.productID
            custname.text = prodObj.productname
            val points = prodObj.points
            custmobile.text = "Credit: $points"
            custaddress.visibility = View.GONE

            edit.setOnClickListener {
                action.OnEditClick(prodObj)
            }
            delete.setOnClickListener {
                action.OnDeleteClick(prodObj)
            }

        }
    }

    interface OnProductClickedListener {
        fun OnEditClick(prodObj: Product)
        fun OnDeleteClick(prodObj: Product)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custpainterlistcardview, parent, false)
        return ProductViewHolder(v)
    }

    override fun getItemCount(): Int {
        return prodList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindItems(prodList[position], clicklistener)
    }
}