package com.example.royal_decor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R

class ViewCreditAdpater(
    val creditList: List<Painters>
) : RecyclerView.Adapter<ViewCreditAdpater.creditViewHolder>() {


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
        return creditList.size
    }

    override fun onBindViewHolder(holder: ViewCreditAdpater.creditViewHolder, position: Int) {
        holder.onBindView(creditList[position])
    }
}