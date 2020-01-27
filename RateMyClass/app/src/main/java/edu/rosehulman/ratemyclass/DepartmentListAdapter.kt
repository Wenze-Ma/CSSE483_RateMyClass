package edu.rosehulman.ratemyclass

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DepartmentListAdapter (var context: Context?,
                             var listener: DepartmentListFragment.OnDepartmentSelectedListener): RecyclerView.Adapter<DepartmentViewHolder>() {

    var departments = ArrayList<Department>()

    init {
        departments.add(Department("Computer Science & Software Engineering", "CSSE", ArrayList()))
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