package edu.rosehulman.ratemyclass

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_COURSE = "course"

class CourseDetailFragment : Fragment() {
    private var course: Course? = null
//    private var listener: OnCourseSelectedListener? = null

    companion object {
        @JvmStatic
        fun newInstance(course: Course) =
            CourseDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_COURSE, course)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getParcelable(ARG_COURSE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_course_detail, container, false)
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
