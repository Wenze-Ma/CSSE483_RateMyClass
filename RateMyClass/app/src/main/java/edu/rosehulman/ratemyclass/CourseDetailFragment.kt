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

private const val ARG_COURSE = "course"
private const val ARG_DEPT = "department"

class CourseDetailFragment : Fragment() {
    private var course: Course? = null
    private var dept: Department? = null
//    private var listener: OnCourseSelectedListener? = null

    companion object {
        @JvmStatic
        fun newInstance(course: Course, dept: Department) =
            CourseDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_COURSE, course)
                    putParcelable(ARG_DEPT, dept)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getParcelable(ARG_COURSE)
            dept = it.getParcelable(ARG_DEPT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val recyclerView = inflater.inflate(R.layout.fragment_course_detail, container, false) as RecyclerView
        val adapter = CourseDetailAdapter(context, dept!!, course!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        return recyclerView
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnCourseSelectedListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }
//
//    interface OnCourseSelectedListener {
//        fun onCourseSelected(course: Course, dept: Department)
//    }
}
