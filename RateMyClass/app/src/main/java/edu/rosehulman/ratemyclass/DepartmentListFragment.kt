package edu.rosehulman.ratemyclass

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_department_list.view.*

class DepartmentListFragment : Fragment() {

    private lateinit var adapter: DepartmentListAdapter
    private var listener: OnDepartmentSelectedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_department_list, container, false)
        adapter = DepartmentListAdapter(context, listener!!)
        val recyclerView = view.dept_recycler_view
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        adapter.addSnapshotListener()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDepartmentSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnDepartmentSelectedListener {
        fun onDepartmentSelected(dept: Department)
    }
}
