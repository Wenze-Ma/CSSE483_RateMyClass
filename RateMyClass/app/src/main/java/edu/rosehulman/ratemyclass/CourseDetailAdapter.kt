package edu.rosehulman.ratemyclass

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.add_comment_view.view.*
import kotlinx.android.synthetic.main.like_dislike_layout.view.*
import kotlinx.android.synthetic.main.post_error.view.*

class CourseDetailAdapter (var context: Context,
                           var dept: Department,
                           var course: Course,
                           var isMine: Boolean,
                           var listener: CourseDetailFragment.OnOKButtonPressed?): RecyclerView.Adapter<CourseDetailViewHolder>() {

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
                    if ((comment.dept == dept.abbr && comment.courseNumber == course.courseNumber) || (isMine && comment.author == User.username)) {
                        when (docChange.type) {
                            DocumentChange.Type.ADDED -> {
                                comments.add(0, comment)
                                notifyItemInserted(0)
                            }

//                            DocumentChange.Type.REMOVED -> {
//                                val pos = comments.indexOfFirst {
//                                    comment.id == it.id
//                                }
//                                comments.removeAt(pos)
//                            }
//
//                            DocumentChange.Type.MODIFIED -> {
//                                val pos = comments.indexOfFirst {
//                                    comment.id == it.id
//                                }
//                                comments[pos] = comment
//                                notifyItemChanged(pos)
//                            }
                        }
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseDetailViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_view, parent, false)
        return CourseDetailViewHolder(view, this)
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

            add(Comment(User.username, content, course.courseNumber, dept.abbr, difficulty, learning, overall, professor, workload, 0,0,
                ArrayList(), ArrayList()
            ))
        }

        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }

    private fun add(comment: Comment) {
        commentsRef.add(comment)
        listener?.sendNotification()
    }

    fun showErrorDialog(type: Int) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.post_error, null, false)
        if (type == 0) {
            view.error_msg.text = context.getString(R.string.error_msg_1)
        }
        if (type == 1) {
            view.error_msg.text = context.getString(R.string.error_msg_2)
        }
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) {_,_ ->
            listener?.onOKButtonPressed()
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }

    fun likeOrDislike(position: Int, like: Boolean, view: View) {
        if (User.username == "") {
            showErrorDialog(1)
            return
        }
        var numLikes = comments[position].like
        val whoLikes = comments[position].whoLikes
        var numDislikes = comments[position].dislike
        val whoDislikes = comments[position].whoDislikes
        val username = User.username
        if (like) {
            if (whoDislikes.contains(username)) {
                whoDislikes.remove(username)
                numDislikes--
            }
            if (whoLikes.contains(username)) {
                whoLikes.remove(username)
                numLikes--
                view.like_button.setImageResource(R.drawable.ic_thumb_up_gray_24dp)
            } else {
                whoLikes.add(username)
                numLikes++
                view.like_button.setImageResource(R.drawable.ic_thumb_up_green_24dp)
            }
            view.dislike_button.setImageResource(R.drawable.ic_thumb_down_gray_24dp)
        } else {
            if (whoLikes.contains(username)) {
                whoLikes.remove(username)
                numLikes--
            }
            if (whoDislikes.contains(username)) {
                whoDislikes.remove(username)
                numDislikes--
                view.dislike_button.setImageResource(R.drawable.ic_thumb_down_gray_24dp)
            } else {
                whoDislikes.add(username)
                numDislikes++
                view.dislike_button.setImageResource(R.drawable.ic_thumb_down_red_24dp)
            }
            view.like_button.setImageResource(R.drawable.ic_thumb_up_gray_24dp)
        }
        comments[position].like = numLikes
        comments[position].whoLikes = whoLikes
        comments[position].dislike = numDislikes
        comments[position].whoDislikes = whoDislikes
        view.like_count.text = numLikes.toString()
        view.dislike_count.text = numDislikes.toString()
        commentsRef.document(comments[position].id).set(comments[position])
    }
}