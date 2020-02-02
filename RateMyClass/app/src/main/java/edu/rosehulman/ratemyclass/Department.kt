package edu.rosehulman.ratemyclass

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class Department (var deptName: String = "", var abbr: String = "") {

    @get:Exclude
    var id = ""

    companion object {
        fun fromSnapshot(snapshot: DocumentSnapshot): Department{
            val dept = snapshot.toObject(Department::class.java)!!
            dept.id = snapshot.id
            return dept
        }
    }
}