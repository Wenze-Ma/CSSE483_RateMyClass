package edu.rosehulman.ratemyclass

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserInfo (var username: String = "", var picture: Int = -1): Parcelable {

    @get:Exclude
    var id = ""

    companion object {
        fun fromSnapshot(snapshot: DocumentSnapshot): UserInfo{
            val userInfo = snapshot.toObject(UserInfo::class.java)!!
            userInfo.id = snapshot.id
            return userInfo
        }
    }
}