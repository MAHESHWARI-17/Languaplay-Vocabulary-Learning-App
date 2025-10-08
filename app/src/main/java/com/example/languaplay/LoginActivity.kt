package com.example.languaplay

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.languaplay.database.UserDatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // If user is already logged in, go to MainActivity
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Initialize UI elements
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        dbHelper = UserDatabaseHelper(this)

        // ✅ Single OnClickListener for Login Button (Fixes Duplicate Issue)
        btnLogin.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))  // Animation Effect

            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            Log.d("LoginDebug", "Login button clicked!")

            if (username.isEmpty() || password.isEmpty()) {
                Log.d("LoginDebug", "Empty fields detected!")
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("LoginDebug", "Checking database for user: $username")

            if (dbHelper.getUser(username, password)) {
                sharedPreferences.edit().apply {
                    putBoolean("isLoggedIn", true)
                    putString("username", username)
                    apply()
                }

                Log.d("LoginDebug", "Login successful for user: $username")
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.d("LoginDebug", "Invalid username or password")
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ Single OnClickListener for Register Button
        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
