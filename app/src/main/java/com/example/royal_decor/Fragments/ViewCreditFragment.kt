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
import com.example.royal_decor.Adapters.ViewCreditAdpater
import com.example.royal_decor.Models.Credits
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants


class ViewCreditFragment : Fragment(), View.OnClickListener {

    private lateinit var v: View
    private lateinit var credlistrv: RecyclerView
    private lateinit var creditadapter: ViewCreditAdpater
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_credit, container, false)

        init()
        initialization()
        val CustListData = fetchingDataForAdapter()
        if (CustListData.size != 0) {
            var sortedList: List<Credits> = CustListData.sortedWith(compareBy {
                it.points
            })
            sortedList = sortedList.reversed()
            settingAdapter(sortedList)
        }


        backImg.setOnClickListener(this)
        return v
    }

    private fun fetchingDataForAdapter(): ArrayList<Credits> {
        val listtobesent = ArrayList<Credits>()

        listtobesent.add(Credits("FAKHRUDDIN", "94564851351", 45))
        listtobesent.add(Credits("JKGBNFJN", "94544851351", 514))
        listtobesent.add(Credits("KJDSNGJFN", "94564851351", 6654))
        listtobesent.add(Credits("JEDFDSJKBDF", "94564851351", 65456))
        listtobesent.add(Credits("LFDNFJDFNKJSF", "94564851351", 5456))


        return listtobesent
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.VIEW_CREDIT_SCORE
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        credlistrv = v.findViewById(R.id.prodlistrv)
    }

    private fun settingAdapter(custListData: List<Credits>) {

        val RecyclerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        credlistrv.layoutManager = RecyclerViewLayoutManager
        creditadapter = ViewCreditAdpater(custListData)
        credlistrv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        credlistrv.adapter = creditadapter
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> {
                activity!!.finish()
            }
        }
    }


}