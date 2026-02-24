package com.example.tbkc.repository

import com.example.tbkc.model.TrekModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TrekRepoImpl : TrekRepo {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.getReference("Treks")

    override fun addTrek(trekModel: TrekModel, callback: (Boolean, String) -> Unit) {
        // Generate a unique key if trekId is empty
        val id = if (trekModel.trekId.isEmpty()) ref.push().key.toString() else trekModel.trekId
        val finalModel = trekModel.copy(trekId = id)

        ref.child(id).setValue(finalModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Trek posted successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateTrek(trekId: String, trekModel: TrekModel, callback: (Boolean, String) -> Unit) {
        ref.child(trekId).updateChildren(trekModel.toMap()).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Trek updated successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun deleteTrek(trekId: String, callback: (Boolean, String) -> Unit) {
        ref.child(trekId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Trek deleted")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getAllTreks(callback: (Boolean, String, List<TrekModel>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val trekList = mutableListOf<TrekModel>()
                for (data in snapshot.children) {
                    val trek = data.getValue(TrekModel::class.java)
                    if (trek != null) trekList.add(trek)
                }
                callback(true, "Treks fetched", trekList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getTreksByUserId(userId: String, callback: (Boolean, String, List<TrekModel>?) -> Unit) {
        ref.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val trekList = mutableListOf<TrekModel>()
                for (data in snapshot.children) {
                    val trek = data.getValue(TrekModel::class.java)
                    if (trek != null) trekList.add(trek)
                }
                callback(true, "User treks fetched", trekList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }
}