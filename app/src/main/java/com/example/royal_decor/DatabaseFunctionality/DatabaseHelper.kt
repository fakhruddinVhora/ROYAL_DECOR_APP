package com.example.royal_decor.DatabaseFunctionality

import com.example.royal_decor.Adapters.PainterListAdapter
import com.example.royal_decor.Models.Painters
import com.example.royal_decor.Utils.Constants
import com.google.firebase.database.*

class DatabaseHelper {

    private lateinit var db: DatabaseReference

    fun open() {
        db = FirebaseDatabase.getInstance().reference
    }

    fun savecreditdetails() {

    }


    fun addproduct() {

    }

    fun updatepainterdetails() {

    }

    fun updateproductdetails() {

    }


    fun addcustomer() {

    }

    fun deletecustomerreview() {

    }

    fun deletepainter(item: Painters, painteradapter: PainterListAdapter) {
        db.child(Constants.NODE_PAINTER).child(item.id).removeValue()
        fetchpainterdetails(painteradapter, true)
    }

    fun deleteproduct() {

    }

    fun fetchpainterdetails(
        painteradapter: PainterListAdapter,
        calledfrominside: Boolean
    ) {
        var list = ArrayList<Painters>()
        var reference: DatabaseReference = db.child(Constants.NODE_PAINTER)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (Snap in snapshot.children) {
                    val obj = Snap.getValue(Painters::class.java)
                    if (obj != null) {
                        list.add(obj)
                    }
                }
                Constants.PAINTER_DB = ArrayList()
                Constants.PAINTER_DB = list
                painteradapter.updatePainterRV(list)
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


    fun fetchcustomerdetails() {

    }

    fun fetchcreditdetails() {

    }


}