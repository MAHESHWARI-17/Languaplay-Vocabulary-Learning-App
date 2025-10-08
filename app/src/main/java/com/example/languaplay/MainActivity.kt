package com.example.languaplay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.ToggleButton


class MainActivity : AppCompatActivity() {
    private lateinit var musicToggle: ToggleButton  // ✅ Declare it here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MusicPlayer.startMusic(this)

        musicToggle = findViewById(R.id.musicToggle)

        musicToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                MusicPlayer.startMusic(this)
            } else {
                MusicPlayer.stopMusic()
            }
        }


        // Get the NavHostFragment and NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup Bottom Navigation with Navigation Controller
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)
    }

    override fun onDestroy() {
        MusicPlayer.stopMusic()
        super.onDestroy()
    }
    
}

