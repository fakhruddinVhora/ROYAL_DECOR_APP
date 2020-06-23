package com.example.royal_decor.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Adapters.PainterATVAdapter
import com.example.royal_decor.Adapters.ProductATVAdapter
import com.example.royal_decor.Adapters.TallyCreditAdapter
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.Models.Product
import com.example.royal_decor.Models.TallyCredit
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.textfield.TextInputEditText


class EvaluateCreditsFragment : Fragment(), View.OnClickListener,
    TallyCreditAdapter.OnPainterClickListener {

    private lateinit var v: View
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var submitbutton: Button
    private lateinit var addbutton: Button
    private lateinit var atvPainter: AutoCompleteTextView
    private lateinit var atvProductName: AutoCompleteTextView
    private lateinit var et_total: TextInputEditText
    private lateinit var et_quantity: TextInputEditText
    private var total = "0"
    private var qtn = "0"
    private var CreditList: ArrayList<TallyCredit> = ArrayList()
    private var sProductCode = ""
    private var sProductPoints = ""
    private var sPainterCode = ""
    private lateinit var tallylistrv: RecyclerView
    private lateinit var tallyadapter: TallyCreditAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_evaluate_credits, container, false)

        init()
        initialization()
        settingPainterAdapter()
        settingProductAdapter()
        setRecyclerView(CreditList)




        backImg.setOnClickListener(this)
        submitbutton.setOnClickListener(this)
        addbutton.setOnClickListener(this)
        return v
    }

    private fun settingPainterAdapter() {
        val adapter = PainterATVAdapter(v.context, R.layout.list_item, fetchData())
        atvPainter.setAdapter(adapter)
        atvPainter.threshold = 1

        atvPainter.setOnItemClickListener { parent, _, position, _ ->
            val selectedPainter = parent.adapter.getItem(position) as Painters
            atvPainter.setText(selectedPainter.name)
            sPainterCode = selectedPainter.id

        }
    }

    private fun settingProductAdapter() {
        val adapter = ProductATVAdapter(v.context, R.layout.list_item, fetchProductData())
        atvProductName.setAdapter(adapter)
        atvProductName.threshold = 1

        atvProductName.setOnItemClickListener { parent, _, position, _ ->
            val selectedProduct = parent.adapter.getItem(position) as Product
            atvProductName.setText(selectedProduct.productname)
            sProductCode = selectedProduct.productID
            sProductPoints = selectedProduct.points
        }
    }

    private fun fetchProductData(): List<Product> {
        val tempList = ArrayList<Product>()

        tempList.add(Product("PRO4334", "Decor", "4"))
        tempList.add(Product("PRO3435", "Decor 2", "3"))
        tempList.add(Product("PRO5454", "Ultima", "5"))
        tempList.add(Product("PRO4534", "Apex", "12"))
        tempList.add(Product("PRO4534", "Apex", "12"))

        return tempList


    }

    private fun fetchData(): List<Painters> {
        var tempList = ArrayList<Painters>()
        if (Constants.PAINTER_DB.size > 0) {
            tempList = Constants.PAINTER_DB
        }
        return tempList
    }

    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.EVALUATE_CREDITS
        et_total.isEnabled = false
        et_total.isClickable = false
        et_total.setTextColor(resources.getColor(R.color.colorPrimary))
    }

    private fun init() {
        backImg = v.findViewById(R.id.img_back)
        headertext = v.findViewById(R.id.header_text)
        submitbutton = v.findViewById(R.id.btn_submitpoints)
        atvPainter = v.findViewById(R.id.atv_painter)
        atvProductName = v.findViewById(R.id.atv_product)
        et_total = v.findViewById(R.id.et_total)
        et_quantity = v.findViewById(R.id.et_quantity)
        tallylistrv = v.findViewById(R.id.tallylistrv)
        addbutton = v.findViewById(R.id.btn_addcreditpoints)
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.img_back -> {
                activity!!.finish()
            }
            R.id.btn_submitpoints -> {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }

            R.id.btn_addcreditpoints -> {
                if (validation()) {
                    val constant = Constants()
                    val total: Int = calculateTotal()
                    CreditList.add(
                        TallyCredit(
                            constant.idGenerator(Constants.isTally),
                            atvProductName.text.toString(),
                            sProductPoints,
                            et_quantity.text.toString(),
                            total.toString()
                        )
                    )
                    tallyadapter.updateRecylerview(CreditList)
                    atvProductName.setText("")
                    atvPainter.isEnabled = false
                    et_quantity.text!!.clear()
                    TallyOutTotal(CreditList)


                }
            }
        }

    }

    private fun TallyOutTotal(creditList: java.util.ArrayList<TallyCredit>) {
        if (creditList.isEmpty()) {
            et_total.setText("0")
            atvPainter.setText("")
            atvPainter.isEnabled = true
            atvPainter.setSelection(0)
        } else {
            var total = 0
            for (i in creditList) {
                total += i.total.toInt()
            }
            et_total.setText(total.toString())
        }
    }

    private fun calculateTotal(): Int {
        return (sProductPoints).toInt() * (et_quantity.text.toString()).toInt()
    }

    private fun setRecyclerView(creditList: ArrayList<TallyCredit>) {
        val RecyclerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        tallylistrv.layoutManager = RecyclerViewLayoutManager
        tallyadapter = TallyCreditAdapter(creditList, this)
        tallylistrv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        tallylistrv.adapter = tallyadapter
    }

    private fun validation(): Boolean {
        var returnBool = true
        if (atvPainter.text.isEmpty()) {
            atvPainter.error = "Please select a painter"
            returnBool = false
        }
        if (atvProductName.text.isEmpty()) {
            atvProductName.error = "Please select a product"
            returnBool = false
        }
        if (et_quantity.text.toString().isEmpty()) {
            et_quantity.error = "error"
            returnBool = false
        }
        return returnBool
    }

    override fun OnDeleteClick(item: TallyCredit) {
        CreditList.remove(item)
        tallyadapter.updateRecylerview(CreditList)
        TallyOutTotal(CreditList)
    }


}