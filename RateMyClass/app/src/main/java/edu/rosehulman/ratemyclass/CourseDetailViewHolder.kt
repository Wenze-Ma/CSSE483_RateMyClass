package edu.rosehulman.ratemyclass

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.comment_view.view.*
import kotlinx.android.synthetic.main.like_dislike_layout.view.*
import kotlinx.android.synthetic.main.profile_view.view.*
import kotlinx.android.synthetic.main.ratings_layout.view.*

class CourseDetailViewHolder(itemView: View, adapter: CourseDetailAdapter) :
    RecyclerView.ViewHolder(itemView) {
    private val username = itemView.user_name as TextView
    private val profname = itemView.professor_name as TextView
    private val content = itemView.comment_content as TextView
    private val overall = itemView.overall_rating as RatingBar
    private val difficulty = itemView.difficulty_rating as RatingBar
    private val learning = itemView.learning_rating as RatingBar
    private val workload = itemView.workload_rating as RatingBar
    private val like = itemView.like_count as TextView
    private val dislike = itemView.dislike_count as TextView
    private val like_button = itemView.like_button as ImageView
    private val dislike_button = itemView.dislike_button as ImageView
    private val author_image = itemView.author_image as ImageView

    private var usersRef: CollectionReference = FirebaseFirestore
        .getInstance()
        .collection("User")

    init {
        itemView.like_button.setOnClickListener {
            adapter.likeOrDislike(adapterPosition, true, itemView)
        }
        itemView.dislike_button.setOnClickListener {
            adapter.likeOrDislike(adapterPosition, false, itemView)
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
        like.text = comment.like.toString()
        dislike.text = comment.dislike.toString()
        if (comment.whoLikes.contains(User.username)) {
            like_button.setImageResource(R.drawable.ic_thumb_up_green_24dp)
        } else {
            like_button.setImageResource(R.drawable.ic_thumb_up_gray_24dp)
        }
        if (comment.whoDislikes.contains(User.username)) {
            dislike_button.setImageResource(R.drawable.ic_thumb_down_red_24dp)
        } else {
            dislike_button.setImageResource(R.drawable.ic_thumb_down_gray_24dp)
        }
        usersRef.get().addOnSuccessListener {
            for (doc in it) {
                val userInfo = UserInfo.fromSnapshot(doc)
                if (userInfo.username == comment.author) {
                    val imageUrl = userInfo.imageUrl
                    GetImageTask(author_image).execute(imageUrl)
                }
            }
        }
    }
}