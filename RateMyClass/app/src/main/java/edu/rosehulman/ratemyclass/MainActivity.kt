package edu.rosehulman.ratemyclass

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(),
                     DepartmentListFragment.OnDepartmentSelectedListener,
                     CourseListFragment.OnCourseSelectedListener{

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
                    true

                }
                else -> false
            }
        }
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

    override fun onDepartmentSelected(dept: Department) {
        Log.d("AAA", "Document selected: ${dept.name}")
        goToCoursePage(dept)
    }

    override fun onCourseSelected(dept: Department, course: Course) {
        Log.d("AAA", "Document selected: ${course.name}")
    }
}
