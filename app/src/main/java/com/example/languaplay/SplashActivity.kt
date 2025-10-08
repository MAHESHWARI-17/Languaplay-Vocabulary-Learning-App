package com.example.languaplay

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Ensure this preference is set somewhere when the user logs in
        val sharedPref: SharedPreferences = getSharedPreferences("LanguaPlay", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java)) // Open Home Screen
            } else {
                startActivity(Intent(this, LoginActivity::class.java)) // Open Login
            }
            finish() // Close SplashActivity to prevent going back to it
        }, 1000) // 3 seconds splash delay
    }
}
