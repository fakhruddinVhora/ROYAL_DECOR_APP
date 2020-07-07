package com.example.royal_decor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.royal_decor.Models.Customers
import com.example.royal_decor.R


class CustomerATVAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private var CustomerList: List<Customers>
) :
    ArrayAdapter<Customers>(context, layoutResource, CustomerList),
    Filterable {
    private var tempList: List<Customers> = CustomerList

    override fun getCount(): Int {
        return tempList.size
    }

    override fun getItem(p0: Int): Customers? {
        return tempList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(layoutResource, parent, false)
        val text = view.findViewById<TextView>(R.id.atv_text)
        val mobileno = view.findViewById<TextView>(R.id.atv_mobile)
        val underlineview = view.findViewById<View>(R.id.underlineview)

        if (position == tempList.size - 1) {
            underlineview.visibility = View.GONE
        }
        if (tempList.size == 1) {
            underlineview.visibility = View.GONE
        }
        text.text = tempList[position].name
        mobileno.text = mobileno.text.toString().replace("XXX", tempList[position].mobileno)


        return view
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: Filter.FilterResults
            ) {
                tempList = filterResults.values as List<Customers>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    CustomerList
                else
                    CustomerList.filter {
                        it.name.toLowerCase().contains(queryString) ||
                                it.mobileno.toLowerCase().contains(queryString)
                    }
                return filterResults
            }
        }
    }
}