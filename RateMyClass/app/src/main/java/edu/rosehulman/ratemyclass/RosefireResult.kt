package edu.rosehulman.ratemyclass

import android.util.Base64
import android.util.Log
import org.json.JSONException
import org.json.JSONObject


class RosefireResult {
    private var username: String? = null
    private var group: String? = null
    private var name: String? = null
    private var email: String? = null
    private var token: String? = null

    fun RosefireResult(token: String?) {
        this.token = token
        if (token == null) {
            return
        }
        val payload = token.split("\\.").toTypedArray()[1]
        try {
            var data = JSONObject(String(Base64.decode(payload, Base64.DEFAULT)))
            if (data.has("d")) { // Old format
                data = data.getJSONObject("d")
            } else {
                username = data.getString("uid")
                data = data.getJSONObject("claims")
                data.put("uid", username)
            }
            username = data.getString("uid")
            group = data.getString("group")
            email = data.getString("email")
            name = data.getString("name")
        } catch (e: JSONException) {
            Log.e("Rosefire", "Could not decode data", e)
        }
    }

    fun isSuccessful(): Boolean {
        return token != null
    }

    fun getUsername(): String? {
        return username
    }

    fun getGroup(): String? {
        return group
    }

    fun getName(): String? {
        return name
    }

    fun getEmail(): String? {
        return email
    }

    fun getToken(): String? {
        return token
    }

}