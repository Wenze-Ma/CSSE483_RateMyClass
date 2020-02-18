package edu.rosehulman.ratemyclass

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_view_hint.view.*

class HintViewHolder(itemView: View, adapter: HintAdapter): RecyclerView.ViewHolder(itemView) {
    private val name = itemView.course_name as TextView

    init {
        itemView.setOnClickListener {
            adapter.selectHintAt(adapterPosition)
        }
    }

    fun bind(course: Course) {
        name.text = course.courseName
    }
}