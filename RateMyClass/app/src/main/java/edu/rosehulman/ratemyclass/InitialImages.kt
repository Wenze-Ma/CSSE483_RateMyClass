package edu.rosehulman.ratemyclass

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object InitialImages {

    private var storageRef = FirebaseStorage.getInstance()
        .reference

    fun loadRandomInitialImages() {
        val random = ((Math.random() * 8) + 1).toInt()

        val imageRef = storageRef.child("images/Initial_Images/random${random}.png")

        imageRef.downloadUrl.addOnSuccessListener {
            val usersRef: CollectionReference = FirebaseFirestore
                .getInstance()
                .collection("User")
            var isFound = false
            val image = it.toString()
            usersRef.get().addOnSuccessListener {
                for (doc in it) {
                    val userInfo = UserInfo.fromSnapshot(doc)
                    if (userInfo.username == User.username) {
                        isFound = true
                        break
                    }
                }
                if (!isFound) {
                    usersRef.add(UserInfo(User.username, image))
                }
            }
        }

    }
}