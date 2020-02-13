package edu.rosehulman.ratemyclass


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.profile_view.view.*

class profileFragment : Fragment() {

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
        return view
    }



}
