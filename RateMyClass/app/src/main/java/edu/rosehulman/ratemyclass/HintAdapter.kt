package edu.rosehulman.ratemyclass

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class HintAdapter (var context: Context?, val input: String, var editText: EditText): RecyclerView.Adapter<HintViewHolder>() {

    private val courses = ArrayList<Course>()
    private val coursesRef: CollectionReference = FirebaseFirestore
        .getInstance()
        .collection("Course")

    init {
        coursesRef
            .addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                if (exception != null) {
                    return@addSnapshotListener
                }
                for (doc in snapshot!!) {
                    val course = Course.fromSnapshot(doc)
                    if (input != "" && input.toLowerCase() in course.courseName.toLowerCase() && input != course.courseName) {
                        courses.add(0, course)
                        notifyItemInserted(0)
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HintViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_hint, parent, false)
        return HintViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: HintViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount() = courses.size

    fun selectHintAt(adapterPosition: Int) {
        Log.d ("AAA",courses[adapterPosition].courseName)
        editText.setText(courses[adapterPosition].courseName)
    }
}