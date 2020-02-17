package edu.rosehulman.ratemyclass

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_course_list.view.*

class CourseListFragment (var dept: Department?, var course: Course?): Fragment() {
    private var listener: OnCourseSelectedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_course_list, container, false)
        val adapter = CourseListAdapter(context, listener!!, dept, course)
        if (dept != null) {
            view.dept_text_view.text = "${dept!!.deptName}:"
        }
        val recyclerView = view.course_recycler_view
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCourseSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnCourseSelectedListener {
        fun onCourseSelected(dept: Department, course: Course)
    }
}