package com.example.royal_decor.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Fragments.CreditStatementFragment
import com.example.royal_decor.Models.TallyLog
import com.example.royal_decor.R


class CreditStatementAdapter(
    var credStmtList: ArrayList<TallyLog>,
    val clickListener: CreditStatementFragment
) : RecyclerView.Adapter<CreditStatementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreditStatementAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custpainterlistcardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CreditStatementAdapter.ViewHolder, position: Int) {
        holder.bindItems(credStmtList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return credStmtList.size
    }

    fun updateData(list: java.util.ArrayList<TallyLog>) {
        credStmtList = list
        notifyDataSetChanged()

    }

    fun deleteData(itemposition: Int) {
        credStmtList.removeAt(itemposition)
        notifyItemRemoved(itemposition)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val custID = itemView.findViewById(R.id.cus_id) as TextView
        val custname = itemView.findViewById(R.id.cus_name) as TextView
        val custaddress = itemView.findViewById(R.id.cus_address) as TextView
        val custmobile = itemView.findViewById(R.id.cus_mobile) as TextView

        val info = itemView.findViewById(R.id.imginfo) as ImageView
        val delete = itemView.findViewById(R.id.imgdelete) as ImageView

        fun bindItems(
            credStmtObj: TallyLog,
            clickListener: CreditStatementFragment
        ) {
            custID.text = "Mob: ${credStmtObj.paintermob}"
            custname.text = credStmtObj.paintername
            custaddress.text = "Total Points: ${credStmtObj.totalPoints.toString()}"
            custmobile.text = credStmtObj.date

            info.setOnClickListener {
                clickListener.OnInfoClick(credStmtObj)
            }
            delete.setOnClickListener {
                clickListener.OnDeleteClick(credStmtObj, adapterPosition)
            }
        }
    }

    interface credStmtOnclickListener {
        fun OnInfoClick(item: TallyLog)
        fun OnDeleteClick(item: TallyLog, position: Int)
    }
}