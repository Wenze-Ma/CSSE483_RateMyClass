package edu.rosehulman.ratemyclass

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.comment_view.view.*
import kotlinx.android.synthetic.main.fragment_my_comments.view.*
import kotlinx.android.synthetic.main.like_dislike_layout.view.*

class MyComments : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_comments, container, false)
        val recyclerView = view.my_comments_recycler_view

        val adapter = CourseDetailAdapter(context!!, Department("", ""), Course("", "", "", ""), true, null)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        return view
    }

}
