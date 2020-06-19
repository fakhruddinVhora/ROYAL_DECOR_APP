package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.royal_decor.Adapters.PainterListAdapter
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants


class PainterListFragment : Fragment(), View.OnClickListener {


    private lateinit var v: View
    private lateinit var viewPager: ViewPager
    private lateinit var painterlistrv: RecyclerView
    private lateinit var custadapter: PainterListAdapter
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_painter_list, container, false)
        init()
        initialization()
        val PainterListData = fetchingDataForAdapter()
        if (PainterListData.size != 0) {
            settingAdapter(PainterListData)
        }
        backImg.setOnClickListener(this)
        return v
    }

    private fun fetchingDataForAdapter(): ArrayList<Painters> {
        val listtobesent = ArrayList<Painters>()

        listtobesent.add(Painters("PAI123", "FAKHRUDDIN", "94564851351", "Anand", ""))
        listtobesent.add(Painters("PAI125", "JKGBNFJN", "94544851351", "Mehsana", ""))
        listtobesent.add(Painters("PAI123", "KJDSNGJFN", "94564851351", "Borsad", ""))
        listtobesent.add(Painters("PAI143", "JEDFDSJKBDF", "94564851351", "Anand", ""))
        listtobesent.add(Painters("PAI173", "LFDNFJDFNKJSF", "94564851351", "Anand", ""))
        listtobesent.add(Painters("PAI113", "FJKDSNJDSF", "94564851351", "Anand", ""))
        listtobesent.add(Painters("PAI123", "FAKHRUDDIN", "94564851351", "Anand", ""))
        listtobesent.add(Painters("PAI125", "JKGBNFJN", "94544851351", "Mehsana", ""))
        listtobesent.add(Painters("PAI123", "KJDSNGJFN", "94564851351", "Borsad", ""))
        listtobesent.add(Painters("PAI143", "JEDFDSJKBDF", "94564851351", "Anand", ""))
        listtobesent.add(Painters("PAI173", "LFDNFJDFNKJSF", "94564851351", "Anand", ""))
        listtobesent.add(Painters("PAI113", "FJKDSNJDSF", "94564851351", "Anand", ""))

        return listtobesent
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.VIEW_PAINTERS_LIST
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        painterlistrv = v.findViewById(R.id.painterlistrv)
    }

    private fun settingAdapter(painterListData: ArrayList<Painters>) {

        val RecyclerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        painterlistrv.layoutManager = RecyclerViewLayoutManager
        custadapter = PainterListAdapter(painterListData) {
            Toast.makeText(activity, it.name, Toast.LENGTH_SHORT).show()
        }
        painterlistrv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        painterlistrv.adapter = custadapter
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> {
                activity!!.finish()
            }
        }
    }


}