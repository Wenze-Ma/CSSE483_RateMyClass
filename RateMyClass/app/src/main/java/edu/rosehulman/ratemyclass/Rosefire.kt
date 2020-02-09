package edu.rosehulman.ratemyclass

import android.content.Context
import android.content.Intent
import edu.rosehulman.rosefire.RosefireResult
import edu.rosehulman.rosefire.WebLoginActivity


class Rosefire {

    var DEBUG = false

    fun getSignInIntent(context: Context?, registryToken: String?): Intent? {
        val intent = Intent(context, WebLoginActivity::class.java)
        intent.putExtra(WebLoginActivity.REGISTRY_TOKEN, registryToken)
        return intent
    }

    fun getSignInResultFromIntent(data: Intent?): RosefireResult? {
        val token = data?.getStringExtra(WebLoginActivity.JWT_TOKEN)
        return RosefireResult(token)
    }
}