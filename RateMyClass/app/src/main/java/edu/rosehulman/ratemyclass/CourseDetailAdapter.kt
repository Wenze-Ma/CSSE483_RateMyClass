package edu.rosehulman.ratemyclass

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class CourseDetailAdapter (var context: Context?,
                           var dept: Department,
                           var course: Course): RecyclerView.Adapter<CourseDetailViewHolder>() {

    private val comments = ArrayList<Comment>()
    private val commentsRef: CollectionReference = FirebaseFirestore
        .getInstance()
        .collection("Comment")

    init {
        commentsRef
            .addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                if (exception != null) {
                    return@addSnapshotListener
                }
                for (doc in snapshot!!) {
                    val comment = Comment.fromSnapshot(doc)
                    if (comment.dept == dept.abbr && comment.courseNumber == course.courseNumber) {
                        comments.add(0, comment)
                        notifyItemInserted(0)
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseDetailViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_view, parent, false)
        return CourseDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseDetailViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount() = comments.size
}