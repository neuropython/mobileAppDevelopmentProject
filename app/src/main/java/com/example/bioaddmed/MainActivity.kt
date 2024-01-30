package com.example.bioaddmed

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bioaddmed.databinding.ActivityMainBinding
import com.example.bioaddmed.ui.article.ArticleFragment
import com.example.bioaddmed.ui.calendar.CalendarFragment
import com.example.bioaddmed.ui.projects.ProjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.container)
        val animationDrawable = constraintLayout.background as AnimationDrawable

        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setOnNavigationItemSelectedListener { item ->
            val newFragment: Fragment = when (item.itemId) {
                R.id.navigation_home -> CalendarFragment() // Replace with your fragment class
                R.id.navigation_dashboard -> ArticleFragment() // Replace with your fragment class
                R.id.navigation_notifications -> ProjectsFragment() // Replace with your fragment class
                else -> CalendarFragment() // Replace with your default fragment class
            }

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            // Load the animations
            val slideUp = AnimationUtils.loadAnimation(this, R.anim.up)
            val slideDown = AnimationUtils.loadAnimation(this, R.anim.down)

            // Set the animations
            fragmentTransaction.setCustomAnimations(R.anim.up, R.anim.down)

            // Replace the fragment
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, newFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            true
        }

        navView.setupWithNavController(navController)
    }
}