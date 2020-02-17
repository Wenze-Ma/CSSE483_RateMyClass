package edu.rosehulman.ratemyclass

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class CourseListAdapter (var context: Context?,
                         var listener: CourseListFragment.OnCourseSelectedListener,
                         var dept: Department?,
                         var course: Course?): RecyclerView.Adapter<CourseViewHolder>() {

    private val courses = ArrayList<Course>()
    private val coursesRef: CollectionReference = FirebaseFirestore
        .getInstance()
        .collection("Course")

    init {
        if (course == null) {
            coursesRef
                .addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                    if (exception != null) {
                        return@addSnapshotListener
                    }
                    for (doc in snapshot!!) {
                        val course = Course.fromSnapshot(doc)
                        if (course.dept == dept!!.abbr) {
                            courses.add(0, course)
                            notifyItemInserted(0)
                        }
                    }
                }
        } else {
            courses.add(0, course!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_dept, parent, false)
        return CourseViewHolder(view, this, context!!)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount() = courses.size

    fun selectCourseAt(adapterPosition: Int) {
        val course = courses[adapterPosition]
        listener.onCourseSelected(dept!!, course)
    }
}