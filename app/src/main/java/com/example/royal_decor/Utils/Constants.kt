package com.example.royal_decor.Utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.R
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class Constants {

    companion object {
        //dashboard
        val ADD_PRODUCT = "Add Product"
        val EVALUATE_CREDITS = "Evaluate Credits"
        val VIEW_PRODUCT = "Products List"
        val ADD_PAINTER = "Add Painter"
        val VIEW_PAINTERS_LIST = "Painters List"
        val VIEW_CUSTOMER_LIST = "Customer List"
        val VIEW_CREDIT_SCORE = "Credit Scores"


        //id generator
        val isCust = "CUS"
        val isPainter = "PAI"
        val isProduct = "PRO"
        val isTally = "TAL"

        //db nodes
        val NODE_CUSTOMER = "Customer"
        val NODE_CREDIT = "Credit"
        val NODE_PAINTER = "Painter"
        val NODE_PRODUCT = "Product"


        //Messages
        val ERROR_FILL_DETAILS = "Enter appropriate values"
        val ERROR_EXCEED_LIMIT = "Dont exceed the limit"


        //Database Values

        var PAINTER_DB: ArrayList<Painters> = ArrayList()


    }

    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        val rand = Random(System.nanoTime())
        return (start..end).random(rand)
    }

    fun idGenerator(idfor: String): String {
        val timeStamp = (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())).toString()
        return idfor + rand(100, 999).toString() + timeStamp
    }


    fun generateSnackBar(c: Context, v: View, text: String) {
        val snack = Snackbar.make(v, text, Snackbar.LENGTH_LONG)
        snack.setAction("Dismiss", View.OnClickListener {
            snack.dismiss()
        })
        snack.setActionTextColor(c.resources.getColor(R.color.icons))
        snack.view.setPadding(0, 0, 0, 0)
        val view: View = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.width = FrameLayout.LayoutParams.MATCH_PARENT
        view.layoutParams = params
        snack.view.setBackgroundColor(c.resources.getColor(R.color.colorPrimary))
        val textView =
            view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 16f
        textView.typeface = ResourcesCompat.getFont(
            c.applicationContext,
            R.font.josefinsansregular
        )
        snack.show()
    }
}