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
import com.example.royal_decor.Adapters.ViewProductAdapter
import com.example.royal_decor.Models.Product
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants


class ViewProductFragment : Fragment(), View.OnClickListener,
    ViewProductAdapter.OnProductClickedListener {

    private lateinit var v: View
    private lateinit var prodlistrv: RecyclerView
    private lateinit var prodadapter: ViewProductAdapter
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_view_product, container, false)
        init()
        initialization()
        val ProdList = fetchingDataForAdapter()
        if (ProdList.size != 0) {
            settingAdapter(ProdList)
        }

        backImg.setOnClickListener(this)
        return v
    }

    private fun fetchingDataForAdapter(): ArrayList<Product> {

        val tempList = ArrayList<Product>()

        tempList.add(Product("PRO4334", "Decor", "4"))
        tempList.add(Product("PRO3435", "Decor 2", "3"))
        tempList.add(Product("PRO5454", "Ultima", "5"))
        tempList.add(Product("PRO4534", "Apex", "12"))

        return tempList
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.VIEW_PRODUCT
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        prodlistrv = v.findViewById(R.id.prodlistrv)
    }

    private fun settingAdapter(prodListData: List<Product>) {

        val RecyclerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        prodlistrv.layoutManager = RecyclerViewLayoutManager
        prodadapter = ViewProductAdapter(prodListData, this)
        prodlistrv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        prodlistrv.adapter = prodadapter
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> {
                activity!!.finish()
            }
        }
    }

    override fun OnEditClick(prodObj: Product) {

    }

    override fun OnDeleteClick(prodObj: Product) {

    }


}