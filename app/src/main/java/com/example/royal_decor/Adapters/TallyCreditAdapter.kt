package com.example.royal_decor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Fragments.EvaluateCreditsFragment
import com.example.royal_decor.Models.TallyCredit
import com.example.royal_decor.R

class TallyCreditAdapter(
    private var tallyList: ArrayList<TallyCredit>,
    private val clickListener: EvaluateCreditsFragment
) : RecyclerView.Adapter<TallyCreditAdapter.viewHolder>() {


    interface OnPainterClickListener {
        fun OnDeleteClick(item: TallyCredit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tallycreditcardview, parent, false)
        return viewHolder(v)
    }

    override fun getItemCount(): Int {
        return tallyList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.Onbind(tallyList[position], clickListener)
    }


    class viewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val prodname = itemView.findViewById(R.id.prodname) as TextView
        val quantity = itemView.findViewById(R.id.quantity) as TextView
        val total = itemView.findViewById(R.id.total) as TextView
        val delete = itemView.findViewById(R.id.imgdelete) as ImageView

        fun Onbind(obj: TallyCredit, action: OnPainterClickListener) {
            prodname.text = obj.productname
            quantity.text = obj.quantity
            total.text = obj.total
            delete.setOnClickListener {
                action.OnDeleteClick(obj)
            }
        }
    }

    fun updateRecylerview(datalist: ArrayList<TallyCredit>) {
        tallyList = datalist
        notifyDataSetChanged()
    }

}

