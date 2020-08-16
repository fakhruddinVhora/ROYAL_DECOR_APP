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


    fun deletecredStmt(
        item: TallyLog,
        progressbar: ProgressBar,
        callback: DataDeletedSuccessCallback
    ) {
        db.child(Constants.NODE_CREDIT_LOGS).child(item.id).removeValue()
            .addOnSuccessListener {
                UpdatePainterData(item, item.painterid, false)
                callback.returnIsDataDeletd(true)
            }
            .addOnFailureListener {
                callback.returnIsDataDeletd(false)
            }

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
                UpdatePainterData(logObj, painterObj.id, true)
            }

    }

    fun UpdatePainterData(logObj: TallyLog, painterid: String, isadd: Boolean) {
        var int = 0
        val ref: DatabaseReference = db.child(Constants.NODE_PAINTER + "/${painterid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val obj = snapshot.getValue(Painters::class.java)
                if (obj != null) {
                    int = obj.credits
                    var total = 0
                    if (isadd) {
                        total = int + logObj.totalPoints
                    } else {
                        if (int >= logObj.totalPoints) {
                            total = int - logObj.totalPoints
                        }
                    }
                    db.child(Constants.NODE_PAINTER).child(painterid).child("credits")
                        .setValue(total)
                    return
                }
            }
        })
    }
/*-------------------------------------------Evaluate Credit DataHandling End------------------------------------------------------------------------*/

    /*-------------------------------------------Product's DataHandling Start------------------------------------------------------------------------*/
    fun addproduct(
        prodObj: Product,
        progressbar: ProgressBar,
        param: DataAddedSuccessCallback
    ): Boolean {
        var returnbool = true

        progressbar.visibility = View.VISIBLE
        //val id = db.push().key
        db.child(Constants.NODE_PRODUCT).child(prodObj.productID).setValue(prodObj)
            .addOnSuccessListener {
                progressbar.visibility = View.GONE
                param.returnIsAddedSuccessfully(true)
            }
            .addOnFailureListener {
                progressbar.visibility = View.GONE
                param.returnIsAddedSuccessfully(false)
            }

        return returnbool
    }

    fun CheckProdCodeForDuplicates(
        prodcode: String,
        progressbar: ProgressBar,
        param: DataAddedSuccessCallback
    ) {
        val query: Query =
            db.child(Constants.NODE_PRODUCT).orderByChild("productcode").equalTo(prodcode)
        progressbar.visibility = View.VISIBLE
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                progressbar.visibility = View.GONE
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                progressbar.visibility = View.GONE
                if (snapshot.exists()) {
                    param.returnIsAddedSuccessfully(false)
                } else {
                    param.returnIsAddedSuccessfully(true)
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
        progressbar: ProgressBar,
        painterObj: Painters,
        param: DataAddedSuccessCallback
    ) {
        progressbar.visibility = View.VISIBLE
        var returnbool = true
        //val id = db.push().key
        db.child(Constants.NODE_PAINTER).child(painterObj.id).setValue(painterObj)
            .addOnSuccessListener {
                progressbar.visibility = View.GONE
                param.returnIsAddedSuccessfully(true)
            }
            .addOnFailureListener {
                progressbar.visibility = View.GONE
                param.returnIsAddedSuccessfully(false)
            }

    }


    fun checkPainterDataForDuplicates(
        progressbar: ProgressBar,
        mobileno: String,
        param: DataAddedSuccessCallback
    ) {
        progressbar.visibility = View.VISIBLE
        val query: Query =
            db.child(Constants.NODE_PAINTER).orderByChild("mobile").equalTo(mobileno);
        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                progressbar.visibility = View.GONE
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                progressbar.visibility = View.GONE
                if (snapshot.exists()) {
                    param.returnIsAddedSuccessfully(false)
                } else {
                    param.returnIsAddedSuccessfully(true)
                }
            }
        })
    }
/*-------------------------------------------Painter's DataHandling End------------------------------------------------------------------------*/


    /*-------------------------------------------Dashboard's DataHandling------------------------------------------------------------------------*/


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


