package edu.rosehulman.ratemyclass

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class MyCommentsAdapter(var context: Context): RecyclerView.Adapter<CourseDetailViewHolder>()  {

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
                    if (comment.author == User.username) {
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

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: CourseDetailViewHolder, position: Int) {
        holder.bind(comments[position])

    }

}