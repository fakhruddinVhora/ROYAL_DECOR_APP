package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Adapters.CustomerListAdapter
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants

class CustomerListFragment : Fragment(), View.OnClickListener,
    CustomerListAdapter.customerOnclickListener {

    private lateinit var v: View
    private lateinit var custlistrv: RecyclerView
    private lateinit var custadapter: CustomerListAdapter
    private lateinit var VerticalLayout: LinearLayoutManager
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_customer_list, container, false)
        init()
        initialization()
        val CustListData = fetchingDataForAdapter()
        if (CustListData.size != 0) {
            settingAdapter(CustListData)
        }


        backImg.setOnClickListener(this)
        return v


    }

    private fun fetchingDataForAdapter(): ArrayList<Painters> {
        val listtobesent = ArrayList<Painters>()

        listtobesent.add(Painters("CUS123", "FAKHRUDDIN", "94564851351", "Anand", ""))
        listtobesent.add(Painters("CUS125", "JKGBNFJN", "94544851351", "Mehsana", ""))
        listtobesent.add(Painters("CUS123", "KJDSNGJFN", "94564851351", "Borsad", ""))
        listtobesent.add(Painters("CUS143", "JEDFDSJKBDF", "94564851351", "Anand", ""))
        listtobesent.add(Painters("CUS173", "LFDNFJDFNKJSF", "94564851351", "Anand", ""))
        listtobesent.add(Painters("CUS113", "FJKDSNJDSF", "94564851351", "Anand", ""))
        listtobesent.add(Painters("CUS123", "FAKHRUDDIN", "94564851351", "Anand", ""))
        listtobesent.add(Painters("CUS125", "JKGBNFJN", "94544851351", "Mehsana", ""))
        listtobesent.add(Painters("CUS123", "KJDSNGJFN", "94564851351", "Borsad", ""))
        listtobesent.add(Painters("CUS143", "JEDFDSJKBDF", "94564851351", "Anand", ""))
        listtobesent.add(Painters("CUS173", "LFDNFJDFNKJSF", "94564851351", "Anand", ""))
        listtobesent.add(Painters("CUS113", "FJKDSNJDSF", "94564851351", "Anand", ""))

        return listtobesent
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.VIEW_CUSTOMER_LIST
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        custlistrv = v.findViewById(R.id.customerlistrv)
    }

    private fun settingAdapter(custListData: ArrayList<Painters>) {

        val RecyclerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        custlistrv.layoutManager = RecyclerViewLayoutManager
        custadapter = CustomerListAdapter(custListData, this)
        custlistrv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        custlistrv.adapter = custadapter
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> {
                activity!!.finish()
            }
        }
    }

    override fun OnDeleteClick(item: Painters) {

    }

    override fun OnInfoClick(item: Painters) {

    }


}
