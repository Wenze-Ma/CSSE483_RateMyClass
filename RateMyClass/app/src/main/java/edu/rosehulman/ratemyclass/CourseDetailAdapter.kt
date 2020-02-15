package edu.rosehulman.ratemyclass

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.add_comment_view.view.*

class CourseDetailAdapter (var context: Context,
                           var dept: Department,
                           var course: Course): RecyclerView.Adapter<CourseDetailViewHolder>() {

    private val comments = ArrayList<Comment>()
    private val commentsRef: CollectionReference = FirebaseFirestore
        .getInstance()
        .collection("Comment")

    init {
        commentsRef
            .orderBy(Comment.TIME_POSTED, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                if (exception != null) {
                    return@addSnapshotListener
                }
                for (docChange in snapshot!!.documentChanges) {
                    val comment = Comment.fromSnapshot(docChange.document)
                    if (comment.dept == dept.abbr && comment.courseNumber == course.courseNumber) {
                        when (docChange.type) {
                            DocumentChange.Type.ADDED -> {
                                comments.add(0, comment)
                                notifyItemInserted(0)
                            }

                            DocumentChange.Type.REMOVED -> {
                                val pos = comments.indexOfFirst {
                                    comment.id == it.id
                                }
                                comments.removeAt(pos)
                            }

                            DocumentChange.Type.MODIFIED -> {
                                val pos = comments.indexOfFirst {
                                    comment.id == it.id
                                }
                                comments[pos] = comment
                                notifyItemChanged(pos)
                            }
                        }
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

    fun showAddEditDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(
            if (position == -1) {
                "Post a Comment"
            } else {
                "Edit a Comment"
            }
        )

        val view = LayoutInflater.from(context).inflate(R.layout.add_comment_view, null, false)
        builder.setView(view)

        view.course_title.text = "${dept.abbr} ${course.courseNumber}"

        builder.setPositiveButton(android.R.string.ok) {_,_ ->
            val professor = view.edit_prof.text.toString()
            val overall = view.overall_rating.rating.toString()
            val difficulty = view.difficulty_rating.rating.toString()
            val learning = view.learning_rating.rating.toString()
            val workload = view.workload_rating.rating.toString()
            val content = view.edit_comment.text.toString()

            add(Comment(User.username, content, course.courseNumber, dept.abbr, difficulty, learning, overall, professor, workload))
        }

        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }

    private fun add(comment: Comment) {
        commentsRef.add(comment)
    }
}