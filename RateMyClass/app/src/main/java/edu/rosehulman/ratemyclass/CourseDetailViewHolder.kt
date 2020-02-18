package edu.rosehulman.ratemyclass

import android.util.Log
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comment_view.view.*
import kotlinx.android.synthetic.main.like_dislike_layout.view.*
import kotlinx.android.synthetic.main.ratings_layout.view.*

class CourseDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val username = itemView.user_name as TextView
    private val profname = itemView.professor_name as TextView
    private val content = itemView.comment_content as TextView
    private val overall = itemView.overall_rating as RatingBar
    private val difficulty = itemView.difficulty_rating as RatingBar
    private val learning = itemView.learning_rating as RatingBar
    private val workload = itemView.workload_rating as RatingBar

    init {
        itemView.like_button.setOnClickListener {
            Log.d("AAA", "clicked")
        }
    }

    fun bind(comment: Comment) {
        username.text = comment.author
        profname.text = comment.professor
        content.text = comment.content
        overall.rating = comment.overall.toFloat()
        difficulty.rating = comment.difficulty.toFloat()
        learning.rating = comment.learning.toFloat()
        workload.rating = comment.workload.toFloat()
    }
}