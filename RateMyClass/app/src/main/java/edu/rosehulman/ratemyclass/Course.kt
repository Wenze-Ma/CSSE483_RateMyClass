package edu.rosehulman.ratemyclass

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class Course (var courseName: String = "", var courseNumber: String = "", var dept: String = "", var score: String = "") {

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