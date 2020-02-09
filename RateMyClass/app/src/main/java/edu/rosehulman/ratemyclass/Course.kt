package edu.rosehulman.ratemyclass

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Course (var courseName: String = "", var courseNumber: String = "", var dept: String = "", var score: String = ""): Parcelable {

    @get:Exclude
    var id = ""

    companion object {
        fun fromSnapshot(snapshot: DocumentSnapshot): Course{
            val course = snapshot.toObject(Course::class.java)!!
            course.id = snapshot.id
            return course
        }
    }
}