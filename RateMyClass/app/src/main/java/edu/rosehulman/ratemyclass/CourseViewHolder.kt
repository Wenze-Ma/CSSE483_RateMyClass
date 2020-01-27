package edu.rosehulman.ratemyclass

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_view_dept.view.*

class CourseViewHolder(itemView: View, adapter: CourseListAdapter, context: Context): RecyclerView.ViewHolder(itemView) {
    private val name = itemView.dept_name as TextView

    init {
        itemView.setOnClickListener {
            adapter.selectCourseAt(adapterPosition)
        }
    }

    fun bind(course: Course) {
        name.text = course.name
    }
}