package edu.rosehulman.ratemyclass

import android.R.attr.button
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_course_list.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private var listener: OnSearchListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        view.submit.setOnClickListener {
            listener?.onClassSearched(view.class_searched.text.toString())
        }
        view.class_searched.setOnFocusChangeListener {_,_ ->
            val layoutParams = view.class_searched.layoutParams as RelativeLayout.LayoutParams
            if (view.class_searched.isFocused) {
                layoutParams.addRule(RelativeLayout.BELOW, view.class_search_title.id)
                view.class_searched.layoutParams = layoutParams
            }

        }
        view.class_searched.addTextChangedListener { text: Editable? ->
            val adapter = HintAdapter(context, view.class_searched.text.toString(), view.class_searched)
            val recyclerView = view.hint_recycler_view
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSearchListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSearchListener {
        fun onClassSearched(classSearched: String)
    }
}
