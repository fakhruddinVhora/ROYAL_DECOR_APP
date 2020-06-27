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
import com.example.royal_decor.Adapters.ViewProductAdapter
import com.example.royal_decor.DatabaseFunctionality.DatabaseHelper
import com.example.royal_decor.Models.Product
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText


class ViewProductFragment : Fragment(), View.OnClickListener,
    ViewProductAdapter.OnProductClickedListener {

    private lateinit var v: View
    private lateinit var prodlistrv: RecyclerView
    private lateinit var prodadapter: ViewProductAdapter
    private lateinit var backImg: ImageView
    private lateinit var headertext: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_view_product, container, false)
        init()
        initialization()
        val ProdList: ArrayList<Product> = ArrayList()
        settingAdapter(ProdList)
        dbHelper.fetchproductdetails(prodadapter, prodlistrv, false)
        backImg.setOnClickListener(this)
        return v
    }


    private fun initialization() {
        backImg.visibility = View.VISIBLE
        headertext.text = Constants.VIEW_PRODUCT
        dbHelper = DatabaseHelper()
        dbHelper.open()
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
        DialogCreator(prodObj)
    }

    override fun OnDeleteClick(prodObj: Product) {
        dbHelper.deleteproduct(prodObj, prodlistrv, prodadapter)
    }

    fun DialogCreator(item: Product) {
        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setTitle("Edit ${item.productname}'s Details")
        val inflater = this.layoutInflater

        val dialogView = inflater.inflate(R.layout.custom_product_dialog_layout, null)

        val name = dialogView.findViewById<TextInputEditText>(R.id.prodname)
        val productcode = dialogView.findViewById<TextInputEditText>(R.id.prodcode)
        val credits = dialogView.findViewById<TextInputEditText>(R.id.credits)



        name.setText(item.productname)
        productcode.setText(item.productcode)
        credits.setText(item.points)

        dialog.setView(dialogView)
        dialog.setPositiveButton("Update") { dialog, which ->
            if (validation(name, productcode, credits)) {
                val editObj = Product(
                    item.productID,
                    productcode.text.toString(),
                    name.text.toString(),
                    credits.text.toString()
                )
                dbHelper.updateproductdetails(editObj, prodlistrv, prodadapter)
            } else {
                Toast.makeText(context, "Not Able to Edit", Toast.LENGTH_SHORT).show()
            }

        }
        dialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun validation(
        name: TextInputEditText,
        et_prodcode: TextInputEditText,
        et_prodcredits: TextInputEditText
    ): Boolean {
        var returnbool = true
        if (name.text!!.isEmpty()) {
            name.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (name.text.toString().length > 50) {
                name.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }

        if (et_prodcredits.text!!.isEmpty()) {
            et_prodcredits.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (et_prodcredits.text.toString().length > 3) {
                et_prodcredits.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }
        if (et_prodcode.text!!.isEmpty()) {
            et_prodcode.error = Constants.ERROR_FILL_DETAILS
            returnbool = false
        } else {
            if (et_prodcode.text.toString().length > 20) {
                et_prodcode.error = Constants.ERROR_EXCEED_LIMIT
                returnbool = false
            }
        }

        return returnbool
    }

}