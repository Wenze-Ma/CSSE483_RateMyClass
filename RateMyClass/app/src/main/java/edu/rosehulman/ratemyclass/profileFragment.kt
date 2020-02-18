package edu.rosehulman.ratemyclass


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.profile_view.view.*

class profileFragment : Fragment() {

    private var listener: OnButtonClicked? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.profile_view, container, false)

        if (User.username == "") {
            view.profile_email.text = "You are not signed in"
        } else {
            view.profile_email.text = "${User.username}@rose-hulman.edu"
        }
        view.button_sign_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            User.username = ""
        }
        view.button_comments.setOnClickListener {
            listener?.onButtonClicked()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnButtonClicked) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnButtonClicked {
        fun onButtonClicked()
    }
}
