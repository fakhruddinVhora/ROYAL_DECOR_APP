package com.example.royal_decor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants

class FunctionalitiesAdapter : BaseAdapter() {


    private val list = getFunctionalityList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Inflate the custom view
        val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.cardviewlayout, null)
       /* view.setMinimumHeight(MainActivity.height / 3);*/

        // Get the custom view widgets reference
        val tv = view.findViewById<TextView>(R.id.tv_name)
        val cdicon = view.findViewById<ImageView>(R.id.cd_icon)

        // Display color name on text view
        tv.text = list[position].first
        cdicon.setBackgroundResource(list[position].second)




        // Finally, return the view
        return view
    }


    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    fun getFunctionalityList(): List<Pair<String, Int>> {
        return listOf(
            Pair(Constants.ADD_PAINTER, R.drawable.ic_addpainter),
            Pair(Constants.ADD_PRODUCT, R.drawable.ic_addproduct),
            Pair(Constants.EVALUATE_CREDITS, R.drawable.ic_evaluatecredits),
            Pair(Constants.UPDATE_DATA, R.drawable.ic_updatecredits),
            Pair(Constants.VIEW_CREDIT_SCORE, R.drawable.ic_viewcredits),
            Pair(Constants.VIEW_PAINTERS_LIST, R.drawable.ic_view_list)
        )
    }
}
