package com.example.royal_decor.DatabaseFunctionality

import android.os.Build
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import com.example.royal_decor.Interface.*
import com.example.royal_decor.Models.Customers
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.Models.Product
import com.example.royal_decor.Models.TallyLog
import com.example.royal_decor.Utils.Constants
import com.google.firebase.database.*


class DatabaseHelper {


    private lateinit var fetchPainterDataRef: DatabaseReference
    private lateinit var fetchProductDataRef: DatabaseReference


    private lateinit var StorePainterDataRef: DatabaseReference
    private lateinit var StoreCustomerDataRef: DatabaseReference
    private lateinit var StoreProductDataRef: DatabaseReference


    private lateinit var db: DatabaseReference


    fun open() {
        db = FirebaseDatabase.getInstance().reference
    }


/*-------------------------------------------CustStatement DataHandling Start------------------------------------------------------------------------*/


    fun getCredStmt(
        progressbar: ProgressBar,
        callback: CredStmtCallback
    ) {
        var list = ArrayList<TallyLog>()

        val fetchCustomerDataRef = db.child(Constants.NODE_CREDIT_LOGS)
        fetchCustomerDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                progressbar.visibility = View.GONE
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (Snap in snapshot.children) {
                    val obj = Snap.getValue(TallyLog::class.java)
                    if (obj != null) {
                        list.add(obj)
                    }
                }
                progressbar.visibility = View.GONE
                callback.returnCredStmtrValues(list)
            }
        })
    }
/*-------------------------------------------Custstatement DataHandling Start------------------------------------------------------------------------*/

/*-------------------------------------------Feedback DataHandling Start------------------------------------------------------------------------*/


    fun addCustomerfeedback(obj: Customers) {
        db.child(Constants.NODE_CUSTOMER).child(obj.id).setValue(obj)
    }

    fun getcustomerdetails(
        progressbar: ProgressBar,
        callback: CustomerCallback
    ) {
        var list = ArrayList<Customers>()

        val fetchCustomerDataRef = db.child(Constants.NODE_CUSTOMER)
        fetchCustomerDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                progressbar.visibility = View.GONE
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (Snap in snapshot.children) {
                    val obj = Snap.getValue(Customers::class.java)
                    if (obj != null) {
                        list.add(obj)
                    }
                }
                progressbar.visibility = View.GONE
                callback.returnCustomerValues(list)
            }
        })
    }

    fun deletecustomer(item: Customers, progressbar: ProgressBar, callback: CustomerCallback) {
        db.child(Constants.NODE_CUSTOMER).child(item.id).removeValue()
        getcustomerdetails(progressbar, callback)
    }


    /*-------------------------------------------Feedback  DataHandling End------------------------------------------------------------------------*/


/*-------------------------------------------Evaluate Credit DataHandling Start------------------------------------------------------------------------*/


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
/*-------------------------------------------Evaluate Credit DataHandling End------------------------------------------------------------------------*/

    /*-------------------------------------------Product's DataHandling Start------------------------------------------------------------------------*/
    fun addproduct(prodObj: Product, param: DataAddedSuccessCallback): Boolean {
        var returnbool = true


        //val id = db.push().key
        db.child(Constants.NODE_PRODUCT).child(prodObj.productID).setValue(prodObj)
            .addOnSuccessListener {
                param.returnCredStmtrValues(true)
            }
            .addOnFailureListener {
                param.returnCredStmtrValues(false)
            }

        return returnbool
    }

    fun getproductdetails(
        progressbar: ProgressBar,
        callback: ProductCallback
    ) {
        var list = ArrayList<Product>()

        fetchPainterDataRef = db.child(Constants.NODE_PRODUCT)
        fetchPainterDataRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                progressbar.visibility = View.GONE
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


    fun deleteproduct(item: Product, progressbar: ProgressBar, callback: ProductCallback) {
        db.child(Constants.NODE_PRODUCT).child(item.productID).removeValue()
        getproductdetails(progressbar, callback)
    }

    fun updateproductdetails(
        editObj: Product,
        progressbar: ProgressBar, callback: ProductCallback
    ) {
        db.child(Constants.NODE_PRODUCT).child(editObj.productID).setValue(editObj)
        getproductdetails(progressbar, callback)
    }
/*-------------------------------------------Product's DataHandling End------------------------------------------------------------------------*/

/*-------------------------------------------Painter's DataHandling Start------------------------------------------------------------------------*/


    fun updatepainterdetails(
        editObj: Painters,
        progressbar: ProgressBar,
        callback: PainterCallback
    ) {
        db.child(Constants.NODE_PAINTER).child(editObj.id).setValue(editObj)
        getpainterdetails(progressbar, callback)
    }

    fun deletepainter(
        item: Painters, progressbar: ProgressBar,
        callback: PainterCallback
    ) {
        db.child(Constants.NODE_PAINTER).child(item.id).removeValue()
        getpainterdetails(progressbar, callback)
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


    fun addpainter(
        painterObj: Painters,
        param: DataAddedSuccessCallback
    ) {

        var returnbool = true
        //val id = db.push().key
        db.child(Constants.NODE_PAINTER).child(painterObj.id).setValue(painterObj)
            .addOnSuccessListener {
                param.returnCredStmtrValues(true)
            }
            .addOnFailureListener {
                param.returnCredStmtrValues(false)
            }

    }
/*-------------------------------------------Painter's DataHandling End------------------------------------------------------------------------*/


    /*-------------------------------------------Dashboard's DataHandling------------------------------------------------------------------------*/


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

/*-------------------------------------------Dashboard's DataHandling------------------------------------------------------------------------*/

}


