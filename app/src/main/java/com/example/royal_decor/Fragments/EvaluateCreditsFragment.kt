package com.example.royal_decor.Fragments

import android.os.AsyncTask
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
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Interface.PainterCallback
import com.example.royal_decor.Interface.ProductCallback
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.Models.Product
import com.example.royal_decor.Models.TallyCredit
import com.example.royal_decor.Models.TallyLog
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class EvaluateCreditsFragment : Fragment(), View.OnClickListener,
    TallyCreditAdapter.OnPainterClickListener {

    private lateinit var dbHelper: DatabaseHelper
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
    private lateinit var tallylistrv: RecyclerView
    private lateinit var tallyadapter: TallyCreditAdapter
    private var PainterObj: Painters? = null
    private var ProductObj: Product? = null
    private lateinit var pb_evaluatecredits: ProgressBar

    private var PainterList: ArrayList<Painters> = ArrayList()
    private var ProductList: ArrayList<Product> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_evaluate_credits, container, false)

        init()
        initialization()
        getDBValues()
        /*  settingPainterAdapter()
          settingProductAdapter()*/
        setRecyclerView(CreditList)




        backImg.setOnClickListener(this)
        submitbutton.setOnClickListener(this)
        addbutton.setOnClickListener(this)
        return v
    }

    private fun getDBValues() {
        PainterList.clear()
        ProductList.clear()
        dbHelper.getpainterdetails(pb_evaluatecredits, object : PainterCallback {
            override fun returnPainterValues(list: ArrayList<Painters>) {
                PainterList = list
                settingPainterAdapter()
            }
        })
        dbHelper.getproductdetails(pb_evaluatecredits, object : ProductCallback {
            override fun returnProductValues(list: ArrayList<Product>) {
                ProductList = list
                settingProductAdapter()
            }

        })
    }

    private fun settingPainterAdapter() {
        val adapter = PainterATVAdapter(v.context, R.layout.list_item, PainterList)
        atvPainter.setAdapter(adapter)
        atvPainter.threshold = 1

        atvPainter.setOnItemClickListener { parent, _, position, _ ->
            val selectedPainter = parent.adapter.getItem(position) as Painters
            atvPainter.setText(selectedPainter.name)
            PainterObj = Painters()
            PainterObj = selectedPainter
        }
    }

    private fun settingProductAdapter() {
        val adapter = ProductATVAdapter(v.context, R.layout.list_item, ProductList)
        atvProductName.setAdapter(adapter)
        atvProductName.threshold = 1

        atvProductName.setOnItemClickListener { parent, _, position, _ ->
            val selectedProduct = parent.adapter.getItem(position) as Product
            atvProductName.setText(selectedProduct.productname)
            ProductObj = Product()
            ProductObj = selectedProduct
            atvProductName.error = null
        }
    }


    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.EVALUATE_CREDITS
        et_total.isEnabled = false
        et_total.isClickable = false
        et_total.setTextColor(resources.getColor(R.color.colorPrimary))
        dbHelper = DatabaseHelper()
        dbHelper.open()
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
        pb_evaluatecredits = v.findViewById(R.id.pb_evaluateCredit)
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.img_back -> {
                activity!!.finish()
            }
            R.id.btn_submitpoints -> {
                if (CreditList.size != 0) {
                    val current = Calendar.getInstance()

                    val simpleFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
                    val date = Date(current.timeInMillis)
                    val dateString = simpleFormat.format(date)
                    val c: Constants = Constants()
                    val logObj = TallyLog(
                        c.idGenerator(Constants.isTally),
                        dateString,
                        PainterObj!!.name,
                        PainterObj!!.id,
                        PainterObj!!.mobile,
                        totalProdOrdered(CreditList),
                        et_total.text.toString().toInt()
                    )
                    dbHelper.addCreditLogs(logObj, PainterObj!!)
                    et_total.setText("0")
                    atvPainter.setText("")
                    atvPainter.isEnabled = true
                    atvProductName.setText("")
                    CreditList.clear()
                    tallyadapter.updateRecylerview(CreditList)
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Please enter something...!!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            R.id.btn_addcreditpoints -> {
                if (validation()) {
                    val constant = Constants()
                    val total: Int = calculateTotal()
                    CreditList.add(
                        TallyCredit(
                            atvProductName.text.toString(),
                            ProductObj!!.points,
                            et_quantity.text.toString(),
                            total.toString()
                        )
                    )
                    tallyadapter.updateRecylerview(CreditList)
                    tallylistrv.scheduleLayoutAnimation()
                    atvProductName.setText("")
                    atvPainter.isEnabled = false
                    et_quantity.text!!.clear()
                    ProductObj == null
                    TallyOutTotal(CreditList)
                }
            }
        }

    }

    private fun totalProdOrdered(creditList: java.util.ArrayList<TallyCredit>): HashMap<String, Int> {
        var returnMap: HashMap<String, Int> = HashMap()
        for (i in creditList) {
            returnMap.put(i.productname, i.quantity.toInt())
        }
        return returnMap
    }

    private fun TallyOutTotal(creditList: java.util.ArrayList<TallyCredit>) {
        if (creditList.isEmpty()) {
            et_total.setText("0")
            atvPainter.setText("")
            ProductObj == null
            PainterObj == null
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
        return (ProductObj!!.points).toInt() * (et_quantity.text.toString()).toInt()
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
        if (PainterObj == null) {
            atvPainter.error = "Please select a painter"
            returnBool = false
        }
        if (ProductObj == null) {
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


    companion object {
        class MyAsyncTask : AsyncTask<Int, String, String?>() {

            override fun onPreExecute() {

            }

            override fun doInBackground(vararg params: Int?): String? {


                return "true"
            }


            override fun onPostExecute(result: String?) {


            }

        }
    }

}

