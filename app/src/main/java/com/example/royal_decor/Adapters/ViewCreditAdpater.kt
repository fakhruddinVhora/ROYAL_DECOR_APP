package com.example.royal_decor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R
import java.util.*

class ViewCreditAdpater(
    var creditList: List<Painters>
) : RecyclerView.Adapter<ViewCreditAdpater.creditViewHolder>(), Filterable {

    var creditFilterList = creditList.toList()

    init {
        creditFilterList = creditList
    }

    class creditViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val paintername = itemview.findViewById<TextView>(R.id.paintername)
        val paintermobno = itemview.findViewById<TextView>(R.id.mobile)
        val credits = itemview.findViewById<TextView>(R.id.creditscore)


        fun onBindView(item: Painters) {
            paintermobno.text = item.mobile
            paintername.text = item.name
            credits.text = item.credits.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewCreditAdpater.creditViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rvviewcreditlayout, parent, false)
        return creditViewHolder(view)
    }

    override fun getItemCount(): Int {
        return creditFilterList.size
    }

    override fun onBindViewHolder(holder: ViewCreditAdpater.creditViewHolder, position: Int) {
        holder.onBindView(creditFilterList[position])
    }

    fun updateCreditAdapter(list: ArrayList<Painters>) {
        creditList = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    creditFilterList = creditList
                } else {
                    val resultList = ArrayList<Painters>()
                    for (row in creditList) {
                        if (row.name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    creditFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = creditFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                creditFilterList = results?.values as ArrayList<Painters>
                notifyDataSetChanged()
            }
        }
    }
}