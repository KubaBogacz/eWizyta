package com.ewizyta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    private lateinit var navControler: NavController
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        recyclerView = findViewById(R.id.recyclerView)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeContainer) as NavHostFragment
        navControler = navHostFragment.navController
        val navigationBarHome = findViewById<BottomNavigationView>(R.id.navigationBarHome)
        NavigationUI.setupWithNavController(navigationBarHome, navControler)

        val doctor1 = Doctor("John", "Doe", 40, "Cardiologist")
        val doctor2 = Doctor("Jane", "Smith", 35, "Pediatrician")
        val doctor3 = Doctor("Michael", "Johnson", 45, "Dermatologist")

        val doctors = listOf(doctor1, doctor2, doctor3)

        doctorAdapter = DoctorAdapter(doctors)
        recyclerView.adapter = doctorAdapter
    }
}