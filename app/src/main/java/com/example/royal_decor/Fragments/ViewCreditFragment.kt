package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
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
    private lateinit var searchImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var searchView: SearchView
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
        searchImg.setOnClickListener(this)
        return v
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.VIEW_CREDIT_SCORE
        searchImg.visibility = View.VISIBLE
        searchImg.setImageDrawable(resources.getDrawable(R.drawable.ic_search))

        val cancelIcon = searchView.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(resources.getColor(R.color.colorPrimary))
        val searchIcon = searchView.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(resources.getColor(R.color.colorPrimary))
        val textView = searchView.findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(resources.getColor(R.color.colorPrimary))
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        credlistrv = v.findViewById(R.id.creditlistrv)
        pb_credit = v.findViewById(R.id.pb_creditview)
        searchImg = v.findViewById(R.id.img_logout)

        searchView = v.findViewById(R.id.creditsearch)


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



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                creditadapter.filter.filter(newText)
                return false
            }

        })
    }


    override fun onClick(v: View) {
        when (v.id) {

            R.id.img_logout -> {
                if (searchView.visibility == View.GONE) {
                    searchView.visibility = View.VISIBLE
                } else {
                    searchView.visibility = View.GONE
                }
            }

            R.id.img_back -> {
                activity!!.finish()
                activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }
    }

    fun onBackPressed() {
        activity!!.finish()
        activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

}