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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.rosefire.Rosefire
import edu.rosehulman.rosefire.RosefireResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(),
                     DepartmentListFragment.OnDepartmentSelectedListener,
                     CourseListFragment.OnCourseSelectedListener,
                     SplashFragment.OnLoginButtonPressedListener,
                     SearchFragment.OnSearchListener {

    private val auth = FirebaseAuth.getInstance()
    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    private val RC_ROSEFIRE_LOGIN = 1001
    private val REGISTRY_TOKEN = "53a4fea9-cc87-44f4-9aae-209bf3891579"

    private var course: Course? = null
    private var department: Department? = null

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
            if (user != null) {
                Log.d("AAA", user.uid)
                User.username = user.uid
            }
            goToSearchPage()
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

    fun getFab(): FloatingActionButton {
        return fab
    }

    private fun switchToSplashFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, SplashFragment())
        ft.commit()
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

    private fun goToCoursePage(dept: Department?, course: Course?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, CourseListFragment(dept, course))
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
        ft.addToBackStack("detail")
        ft.commit()
    }

    private fun goToProfilePage() {
        if (User.username != "") {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, profileFragment())
            ft.commit()
        } else {
            switchToSplashFragment()
        }
    }

    override fun onDepartmentSelected(dept: Department) {
        Log.d("AAA", "Document selected: ${dept.deptName}")
        goToCoursePage(dept, null)
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
        val signInIntent: Intent = Rosefire.getSignInIntent(this, REGISTRY_TOKEN)
        startActivityForResult(signInIntent, RC_ROSEFIRE_LOGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ROSEFIRE_LOGIN) {
            val result: RosefireResult = Rosefire.getSignInResultFromIntent(data)
            if (result.isSuccessful) {
                Log.d("AAA", result.username)
                auth.signInWithCustomToken(result.token).addOnCompleteListener(this, OnCompleteListener {
                    Log.d("AAA", "Login succeeded")
                })
            }
        }
    }

    override fun onClassSearched(classSearched: String) {
        findCourse(classSearched)
        Log.d("AAA", course.toString())
        Log.d("AAA", department.toString())
        if (department != null) {
            goToCoursePage(department, course)
        }
    }

    private fun findCourse(classSearched: String) {
        val coursesRef: CollectionReference = FirebaseFirestore
            .getInstance()
            .collection("Course")

        coursesRef.whereEqualTo("courseName", classSearched)
            .get()
            .addOnSuccessListener {documents ->
                for (doc in documents) {
                    course = Course.fromSnapshot(doc)
                }
            }
        val deptRef: CollectionReference = FirebaseFirestore
            .getInstance()
            .collection("Department")

        deptRef.whereEqualTo("abbr", course?.dept)
            .get()
            .addOnSuccessListener { documents->
                for (doc in documents) {
                    department = Department.fromSnapshot(doc)
                }
            }

    }
}
