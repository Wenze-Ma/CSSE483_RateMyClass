package edu.rosehulman.ratemyclass

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DepartmentListAdapter (var context: Context?,
                             var listener: DepartmentListFragment.OnDepartmentSelectedListener): RecyclerView.Adapter<DepartmentViewHolder>() {

    var departments = ArrayList<Department>()
    var temp = ArrayList<Course>()

    init {
        temp.add(Course("1", 1, ArrayList()))
        temp.add(Course("2", 1, ArrayList()))
        temp.add(Course("3", 1, ArrayList()))
        temp.add(Course("4", 1, ArrayList()))
        departments.add(Department("Computer Science & Software Engineering", "CSSE", temp))
        departments.add(Department("Mathematics", "MA", ArrayList()))
        departments.add(Department("Computer Engineering", "CPE", ArrayList()))
        departments.add(Department("Mechanical Engineering", "ME", ArrayList()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_dept, parent, false)
        return DepartmentViewHolder(view, this, context!!)
    }

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        holder.bind(departments[position])
    }

    override fun getItemCount() = departments.size

    fun selectDeptAt(adapterPosition: Int) {
        val question = departments[adapterPosition]
        listener.onDepartmentSelected(question)
    }
}