package com.example.languaplay

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val highScoreText: TextView = findViewById(R.id.highScoreProfileText)
        val sharedPreferences = getSharedPreferences("HighScore", MODE_PRIVATE)
        val highScore = sharedPreferences.getInt("highScore", 0)

        highScoreText.text = "High Score: $highScore"
    }
}
