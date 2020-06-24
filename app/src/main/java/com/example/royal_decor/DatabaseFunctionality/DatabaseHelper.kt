package com.example.royal_decor.DatabaseFunctionality

import com.example.royal_decor.Adapters.PainterListAdapter
import com.example.royal_decor.Adapters.ViewProductAdapter
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.Models.Product
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


    fun updatepainterdetails() {

    }

    fun updateproductdetails() {

    }


    fun addcustomer() {

    }

    fun deletecustomerreview() {

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
                if (removelistener) {
                    fetchProductDataRef.removeEventListener(this)
                }
            }
        })
    }

    fun deleteproduct(item: Product, prodAdapter: ViewProductAdapter) {
        db.child(Constants.NODE_PRODUCT).child(item.productID).removeValue()
        fetchproductdetails(prodAdapter, true)

    }


    //painters
    fun deletepainter(item: Painters, painteradapter: PainterListAdapter) {
        db.child(Constants.NODE_PAINTER).child(item.id).removeValue()
        fetchpainterdetails(painteradapter, true)
    }

    fun fetchpainterdetails(
        painteradapter: PainterListAdapter,
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
                if (showinrecyler) {
                    fetchPainterDataRef.removeEventListener(this)
                }
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


    fun storeDBValuesInConstants() {

        //PainterData
        StorePainterDataRef = db.child(Constants.NODE_PAINTER)
        StorePainterDataRef.addValueEventListener(object : ValueEventListener {
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
            }

        })

        //Product Data
        StoreProductDataRef = db.child(Constants.NODE_PRODUCT)
        StoreProductDataRef.addValueEventListener(object : ValueEventListener {
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
            }

        })
    }


}