package com.example.royal_decor.DatabaseFunctionality

import android.os.Build
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.royal_decor.Adapters.PainterListAdapter
import com.example.royal_decor.Adapters.ViewProductAdapter
import com.example.royal_decor.Interface.PainterCallback
import com.example.royal_decor.Interface.PiechartCallback
import com.example.royal_decor.Interface.ProductCallback
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.Models.Product
import com.example.royal_decor.Models.TallyLog
import com.example.royal_decor.Utils.Constants
import com.google.firebase.database.*


class DatabaseHelper {

    companion object {
        private lateinit var fetchPainterDataRef: DatabaseReference
        private lateinit var fetchProductDataRef: DatabaseReference


        private lateinit var StorePainterDataRef: DatabaseReference
        private lateinit var StoreCustomerDataRef: DatabaseReference
        private lateinit var StoreProductDataRef: DatabaseReference


        private lateinit var db: DatabaseReference


    }


    fun open() {
        db = FirebaseDatabase.getInstance().reference
    }

    fun fetchcustomerdetails() {

    }

    fun fetchcreditdetails() {

    }


    fun savecreditdetails() {

    }


    fun addcustomer() {

    }

    fun deletecustomerreview() {

    }


    //creditsLog

    fun addCreditLogs(
        logObj: TallyLog,
        painterObj: Painters
    ) {
        db.child(Constants.NODE_CREDIT_LOGS).child(logObj.id).setValue(logObj)
            .addOnSuccessListener {
                UpdatePainterData(logObj, painterObj)
            }

    }

    fun UpdatePainterData(logObj: TallyLog, painterObj: Painters) {
        var int = 0
        val ref: DatabaseReference = db.child(Constants.NODE_PAINTER + "/${painterObj.id}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val obj = snapshot.getValue(Painters::class.java)
                if (obj != null) {
                    int = obj.credits
                    var total = int + logObj.totalPoints
                    db.child(Constants.NODE_PAINTER).child(painterObj.id).child("credits")
                        .setValue(total)
                    return
                }
            }
        })
    }


    //product
    fun addproduct(prodObj: Product): Boolean {
        var returnbool = true
        //val id = db.push().key
        db.child(Constants.NODE_PRODUCT).child(prodObj.productID).setValue(prodObj)
            .addOnSuccessListener {
                returnbool = true
            }
            .addOnFailureListener {
                returnbool = false
            }

        return returnbool
    }


    fun fetchproductdetails(
        prodAdapter: ViewProductAdapter,
        rv: RecyclerView,
        removelistener: Boolean
    ) {
        var list = ArrayList<Product>()
        fetchProductDataRef = db.child(Constants.NODE_PRODUCT)
        fetchProductDataRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (Snap in snapshot.children) {
                    val obj = Snap.getValue(Product::class.java)
                    if (obj != null) {
                        list.add(obj)
                    }
                }
                Constants.PRODUCT_DB.clear()
                Constants.PRODUCT_DB = list
                prodAdapter.updateProductRV(list)
                rv.scheduleLayoutAnimation()
                if (removelistener) {
                    fetchProductDataRef.removeEventListener(this)
                }
            }
        })
    }

    fun deleteproduct(item: Product, rv: RecyclerView, prodAdapter: ViewProductAdapter) {
        db.child(Constants.NODE_PRODUCT).child(item.productID).removeValue()
        fetchproductdetails(prodAdapter, rv, true)
    }

    fun updateproductdetails(
        editObj: Product,
        rv: RecyclerView,
        prodAdapter: ViewProductAdapter
    ) {
        db.child(Constants.NODE_PRODUCT).child(editObj.productID).setValue(editObj)
        fetchproductdetails(prodAdapter, rv, true)
    }


    //painters
    fun updatepainterdetails(
        editObj: Painters,
        rv: RecyclerView,
        painteradapter: PainterListAdapter
    ) {
        db.child(Constants.NODE_PAINTER).child(editObj.id).setValue(editObj)
        fetchpainterdetails(painteradapter, rv, true)
    }

    fun deletepainter(item: Painters, rv: RecyclerView, painteradapter: PainterListAdapter) {
        db.child(Constants.NODE_PAINTER).child(item.id).removeValue()
        fetchpainterdetails(painteradapter, rv, true)
    }

    fun fetchpainterdetails(
        painteradapter: PainterListAdapter,
        rv: RecyclerView,
        showinrecyler: Boolean
    ) {
        var list = ArrayList<Painters>()
        fetchPainterDataRef = db.child(Constants.NODE_PAINTER)



        fetchPainterDataRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (Snap in snapshot.children) {
                    val obj = Snap.getValue(Painters::class.java)
                    if (obj != null) {
                        list.add(obj)
                    }
                }
                Constants.PAINTER_DB.clear()
                Constants.PAINTER_DB = list
                painteradapter.updatePainterRV(list)
                rv.scheduleLayoutAnimation()
                if (showinrecyler) {
                    fetchPainterDataRef.removeEventListener(this)
                }
            }
        })
    }

    fun getproductdetails(
        progressbar: ProgressBar,
        callback: ProductCallback
    ) {
        var list = ArrayList<Product>()

        fetchPainterDataRef = db.child(Constants.NODE_PRODUCT)
        fetchPainterDataRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (Snap in snapshot.children) {
                    val obj = Snap.getValue(Product::class.java)
                    if (obj != null) {
                        list.add(obj)
                    }
                }
                progressbar.visibility = View.GONE
                callback.returnProductValues(list)
            }
        })
    }

    fun getpainterdetails(
        progressbar: ProgressBar,
        callback: PainterCallback
    ) {
        var list = ArrayList<Painters>()
        progressbar.visibility = View.VISIBLE
        fetchPainterDataRef = db.child(Constants.NODE_PAINTER)
        fetchPainterDataRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                progressbar.visibility = View.GONE
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (Snap in snapshot.children) {
                    val obj = Snap.getValue(Painters::class.java)
                    if (obj != null) {
                        list.add(obj)
                    }
                }
                progressbar.visibility = View.GONE
                callback.returnPainterValues(list)
            }
        })
    }


    fun addpainter(painterObj: Painters): Boolean {

        var returnbool = true
        //val id = db.push().key
        db.child(Constants.NODE_PAINTER).child(painterObj.id).setValue(painterObj)
            .addOnSuccessListener {
                returnbool = true
            }
            .addOnFailureListener {
                returnbool = false
            }

        return returnbool
    }


    fun storeDBValuesInConstants(dashboardprogressbar: ProgressBar) {

        //PainterData
        StorePainterDataRef = db.child(Constants.NODE_PAINTER)
        StorePainterDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Constants.PAINTER_DB.clear()
                val dataObj = snapshot.children
                for (d in dataObj) {
                    val obj = d.getValue(Painters::class.java)
                    if (obj != null) {
                        Constants.PAINTER_DB.add(obj)
                    }
                }
                fetchProdDetails(dashboardprogressbar)
            }

        })

    }

    private fun fetchProdDetails(dashboardprogressbar: ProgressBar) {

        //Product Data
        StoreProductDataRef = db.child(Constants.NODE_PRODUCT)
        StoreProductDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Constants.PRODUCT_DB.clear()
                val dataObj = snapshot.children
                for (d in dataObj) {
                    val obj = d.getValue(Product::class.java)
                    if (obj != null) {
                        Constants.PRODUCT_DB.add(obj)
                    }
                }
                dashboardprogressbar.visibility = View.GONE
            }

        })
    }


    fun fetchDataforPieChart(loginprogressbar: ProgressBar, piechartcallback: PiechartCallback) {
        val pieChrtDataRef: DatabaseReference = db.child(Constants.NODE_CREDIT_LOGS)
        pieChrtDataRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                var list: ArrayList<HashMap<String, Int>> = ArrayList()
                for (Data in snapshot.children) {
                    val obj = Data.getValue(TallyLog::class.java)
                    if (obj != null) {
                        list.add(obj.productMap)
                    }
                }
                Constants.PIECHART_PROD_DATA.clear()
                // Constants.PIECHART_PROD_DATA = list
                loginprogressbar.visibility = View.GONE
                piechartcallback.returnPieChartValues(list)
                return
            }

        })
    }


}


