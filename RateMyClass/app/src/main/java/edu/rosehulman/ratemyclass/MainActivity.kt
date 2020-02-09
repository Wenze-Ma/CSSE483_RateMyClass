package edu.rosehulman.ratemyclass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import edu.rosehulman.rosefire.Rosefire
import edu.rosehulman.rosefire.RosefireResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(),
                     DepartmentListFragment.OnDepartmentSelectedListener,
                     CourseListFragment.OnCourseSelectedListener,
                     SplashFragment.OnLoginButtonPressedListener {

    private val auth = FirebaseAuth.getInstance()
    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    private val RC_ROSEFIRE_LOGIN = 1001
    private val REGISTRY_TOKEN = "acdd05d7-d0dc-4cdb-aee0-0f2dd3819559"


    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        goToSearchPage()

        nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    goToSearchPage()
                    true

                }
                R.id.navigation_dashboard -> {
                    goToDeptPage()
                    true
                }
                R.id.navigation_notifications -> {
                    goToProfilePage()
                    true
                }
                else -> false
            }
        }

        initializeListeners()

        fab.hide()
    }

    private fun initializeListeners() {
        authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            Log.d("AAA", "In auth listener, user = $user")
//            if (user != null) {
                goToSearchPage()
//            } else {
//                switchToSplashFragment()
//            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }
    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }


    private fun switchToSplashFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, SplashFragment())
        ft.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToSearchPage() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, SearchFragment())
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
        ft.commit()
    }

    private fun goToDeptPage() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, DepartmentListFragment())
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
        ft.commit()
    }

    private fun goToCoursePage(dept: Department) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, CourseListFragment(dept))
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
        ft.addToBackStack("detail")
        ft.commit()
    }

    private fun goToProfilePage() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, profileFragment())
        ft.commit()
    }

    override fun onDepartmentSelected(dept: Department) {
        Log.d("AAA", "Document selected: ${dept.deptName}")
        goToCoursePage(dept)
    }

    override fun onCourseSelected(dept: Department, course: Course) {
        Log.d("AAA", "Document selected: ${course.courseName}")
        val courseDetailFragment = CourseDetailFragment.newInstance(course, dept)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, courseDetailFragment)
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun onLoginButtonPressed() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val loginIntent =  AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_launcher_custom)
            .build()

        val signInIntent: Intent = Rosefire.getSignInIntent(this, REGISTRY_TOKEN)
        startActivityForResult(signInIntent, RC_ROSEFIRE_LOGIN)

        // Create and launch sign-in intent
//        startActivityForResult(loginIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ROSEFIRE_LOGIN) {
            val result: RosefireResult = Rosefire.getSignInResultFromIntent(data)
            if (!result.isSuccessful) {
                Log.d("AAA", "The user cancelled the login")
                return
            }
            FirebaseAuth.getInstance().signInWithCustomToken(result.token)
                .addOnCompleteListener(this, OnCompleteListener {

                })
        }
    }
}
