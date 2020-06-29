package com.example.royal_decor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R
import java.util.*
import kotlin.collections.ArrayList


class PainterListAdapter(
    var painterlist: ArrayList<Painters>,
    var painterclickListener: OnPainterClickListener
) : RecyclerView.Adapter<PainterListAdapter.ViewHolder>(), Filterable {

    var painterFilterList = ArrayList<Painters>()

    init {
        painterFilterList = painterlist
    }


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
        holder.bindItems(painterFilterList[position], painterclickListener)
    }

    fun updatePainterRV(s: ArrayList<Painters>) {
        painterFilterList = s
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return painterFilterList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val custID = itemView.findViewById(R.id.cus_id) as TextView
        val custname = itemView.findViewById(R.id.cus_name) as TextView
        val custaddress = itemView.findViewById(R.id.cus_address) as TextView
        val custmobile = itemView.findViewById(R.id.cus_mobile) as TextView
        val edit = itemView.findViewById(R.id.imginfo) as ImageView
        val delete = itemView.findViewById(R.id.imgdelete) as ImageView


        fun bindItems(painterObj: Painters, action: OnPainterClickListener) {

            edit.setImageResource(0)
            edit.setBackgroundResource(R.drawable.ic_edit)
            custID.text = painterObj.id
            custname.text = painterObj.name
            custaddress.text = painterObj.address
            custmobile.text = painterObj.mobile

            edit.setOnClickListener {
                action.OnEditClick(painterObj)
            }
            delete.setOnClickListener {
                action.OnDeleteClick(painterObj)
            }

        }
    }

    interface OnPainterClickListener {
        fun OnDeleteClick(item: Painters)
        fun OnEditClick(item: Painters)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    painterFilterList = painterlist
                } else {
                    val resultList = ArrayList<Painters>()
                    for (row in painterlist) {
                        if (row.name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    painterFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = painterFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                painterFilterList = results?.values as ArrayList<Painters>
                notifyDataSetChanged()
            }

        }

    }
}