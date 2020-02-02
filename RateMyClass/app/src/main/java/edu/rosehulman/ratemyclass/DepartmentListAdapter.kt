package edu.rosehulman.ratemyclass

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class DepartmentListAdapter (var context: Context?,
                             var listener: DepartmentListFragment.OnDepartmentSelectedListener): RecyclerView.Adapter<DepartmentViewHolder>() {

    var departments = ArrayList<Department>()

    private lateinit var listenerRegistration: ListenerRegistration

    private val deptRef: CollectionReference = FirebaseFirestore
        .getInstance()
        .collection("Department")


    fun addSnapshotListener() {
        listenerRegistration = deptRef
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.w("AAA", "listen error", e)
                } else {
                    processSnapshotChanges(querySnapshot!!)
                }
            }
    }

    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        for (document in querySnapshot) {
            val dept = Department.fromSnapshot(document)
            departments.add(0, dept)
            notifyItemInserted(0)
        }
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