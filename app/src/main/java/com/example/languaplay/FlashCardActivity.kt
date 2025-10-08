package com.example.languaplay

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AnimationUtils

class FlashcardActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var wordText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        dbHelper = DatabaseHelper(this)
        wordText = findViewById(R.id.wordText)
        val nextButton: Button = findViewById(R.id.nextButton)
        val backButton: Button = findViewById(R.id.backButton)

        showNewWord() // Load the first word

        nextButton.setOnClickListener {
            showNewWord() // Load a new word when Next is clicked
        }

        backButton.setOnClickListener {
            finish() // Go back to the previous screen
        }
    }

    private fun showNewWord() {
        val newWord = dbHelper.getRandomFlashcardWord()

        if (newWord != null) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            wordText.startAnimation(animation)
            wordText.text = newWord
        } else {
            Toast.makeText(this, "No words available!", Toast.LENGTH_SHORT).show()
        }
    }
}
