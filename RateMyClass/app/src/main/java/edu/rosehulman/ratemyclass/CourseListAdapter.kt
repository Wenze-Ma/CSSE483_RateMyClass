package edu.rosehulman.ratemyclass

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CourseListAdapter (var context: Context?,
                         var listener: CourseListFragment.OnCourseSelectedListener,
                         var dept: Department): RecyclerView.Adapter<CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_dept, parent, false)
        return CourseViewHolder(view, this, context!!)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(dept.courses[position])
    }

    override fun getItemCount() = dept.courses.size

    fun selectCourseAt(adapterPosition: Int) {
        val course = dept.courses[adapterPosition]
        listener.onCourseSelected(dept, course)
    }
}