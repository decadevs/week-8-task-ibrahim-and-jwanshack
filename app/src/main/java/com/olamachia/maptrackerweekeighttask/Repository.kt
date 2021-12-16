package com.olamachia.maptrackerweekeighttask

import android.util.Log
import com.google.firebase.database.*
import com.olamachia.maptrackerweekeighttask.models.LocationModel


class Repository {

 object FirebaseManipulation {

     private lateinit var database: FirebaseDatabase
     private lateinit var reference: DatabaseReference



     fun addData(location: LocationModel){
         database = FirebaseDatabase.getInstance()
         reference = database.getReference("Users")
         location.id = "Shak"
         reference.child(location.id!!).setValue(location)
     }

 }
}
