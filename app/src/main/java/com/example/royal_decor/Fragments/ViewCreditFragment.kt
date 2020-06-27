package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Adapters.ViewCreditAdpater
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Interface.PainterCallback
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants


class ViewCreditFragment : Fragment(), View.OnClickListener {

    private lateinit var v: View
    private lateinit var credlistrv: RecyclerView
    private lateinit var creditadapter: ViewCreditAdpater
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var pb_credit: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_credit, container, false)

        init()
        initialization()
        dbHelper.getpainterdetails(pb_credit, object : PainterCallback {
            override fun returnPainterValues(list: ArrayList<Painters>) {
                if (list.size != 0) {
                    var sortedList: List<Painters> = list.sortedWith(compareBy {
                        it.credits
                    })
                    sortedList = sortedList.reversed()
                    settingAdapter(sortedList)
                }
            }

        })
        backImg.setOnClickListener(this)
        return v
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.VIEW_CREDIT_SCORE
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        credlistrv = v.findViewById(R.id.creditlistrv)
        pb_credit = v.findViewById(R.id.pb_creditview)


        dbHelper = DatabaseHelper()
        dbHelper.open()
    }

    private fun settingAdapter(painterlistData: List<Painters>) {

        val RecyclerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        credlistrv.layoutManager = RecyclerViewLayoutManager
        creditadapter = ViewCreditAdpater(painterlistData)
        credlistrv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        credlistrv.adapter = creditadapter
        credlistrv.scheduleLayoutAnimation()
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> {
                activity!!.finish()
            }
        }
    }


}