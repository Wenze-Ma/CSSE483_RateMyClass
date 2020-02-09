package edu.rosehulman.ratemyclass

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Department (var deptName: String = "", var abbr: String = ""): Parcelable {

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