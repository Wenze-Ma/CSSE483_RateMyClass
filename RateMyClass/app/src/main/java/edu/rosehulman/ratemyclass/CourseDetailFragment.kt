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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_course_detail.view.*

private const val ARG_COURSE = "course"
private const val ARG_DEPT = "department"

class CourseDetailFragment: Fragment() {
    private var course: Course? = null
    private var dept: Department? = null

    private var listener: OnOKButtonPressed? = null


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

    override fun onStop() {
        super.onStop()
        activity!!.fab.hide()
    }

    override fun onStart() {
        super.onStart()
        activity!!.fab.show()
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
        val view = inflater.inflate(R.layout.fragment_course_detail, container, false)
        val recyclerView = view.course_detail_recycler_view
        val adapter = CourseDetailAdapter(context!!, dept!!, course!!, false, listener)
        view.comment_text_view.text = "${course!!.courseName}:"
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        (context as MainActivity).getFab().setOnClickListener {
            if (User.username != "") {
                adapter.showAddEditDialog(-1)
            } else {
                adapter.showErrorDialog(0)
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOKButtonPressed) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnOKButtonPressed {
        fun onOKButtonPressed()
    }
}
