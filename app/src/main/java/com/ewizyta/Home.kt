package com.ewizyta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    private lateinit var navControler: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeContainer) as NavHostFragment
        navControler = navHostFragment.navController
        val navigationBarHome = findViewById<BottomNavigationView>(R.id.navigationBarHome)
        NavigationUI.setupWithNavController(navigationBarHome, navControler)
    }
}