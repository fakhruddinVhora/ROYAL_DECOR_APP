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
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants


class PainterListFragment : Fragment(), View.OnClickListener,
    PainterListAdapter.OnPainterClickListener {


    private lateinit var v: View
    private lateinit var viewPager: ViewPager
    private lateinit var painterlistrv: RecyclerView
    private lateinit var painteradapter: PainterListAdapter
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var dbHelper: DatabaseHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_painter_list, container, false)
        init()
        initialization()
        val list = ArrayList<Painters>()
        settingAdapter(list)
        dbHelper.fetchpainterdetails(painteradapter, false)
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
        dbHelper = DatabaseHelper()
        dbHelper.open()
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        painterlistrv = v.findViewById(R.id.painterlistrv)
    }

    fun settingAdapter(painterListData: ArrayList<Painters>) {

        val RecyclerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        painterlistrv.layoutManager = RecyclerViewLayoutManager
        painteradapter = PainterListAdapter(painterListData, this)
        painterlistrv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        painterlistrv.adapter = painteradapter
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> {
                activity!!.finish()
            }
        }
    }

    override fun OnDeleteClick(item: Painters) {
        val result = dbHelper.deletepainter(item, painteradapter)
    }

    override fun OnEditClick(item: Painters) {
        Toast.makeText(activity, "EDIT::$item.name", Toast.LENGTH_SHORT).show()
    }


}