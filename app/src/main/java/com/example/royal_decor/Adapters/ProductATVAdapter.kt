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
import com.example.royal_decor.Models.Product
import com.example.royal_decor.R

class ProductATVAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private val ProdList: List<Product>
) :
    ArrayAdapter<Product>(context, layoutResource, ProdList),
    Filterable {
    private var tempList: List<Product> = ProdList

    override fun getCount(): Int {
        return tempList.size
    }

    override fun getItem(p0: Int): Product? {
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

        if (tempList.size == 1) {
            underlineview.visibility = View.GONE
        }
        text.text = tempList[position].productname
        mobileno.text = tempList[position].productcode


        return view
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: Filter.FilterResults
            ) {
                tempList = filterResults.values as List<Product>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    ProdList
                else
                    ProdList.filter {
                        it.productname.toLowerCase().contains(queryString) ||
                                it.productcode.toLowerCase().contains(queryString)
                    }
                return filterResults
            }
        }
    }
}