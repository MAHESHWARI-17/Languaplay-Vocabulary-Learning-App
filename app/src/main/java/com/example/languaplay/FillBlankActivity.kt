package com.example.languaplay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FillBlankActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var correctAnswer: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_blank)

        dbHelper = DatabaseHelper(this)

        val sentenceText: TextView = findViewById(R.id.sentenceText)
        val answerInput: EditText = findViewById(R.id.answerInput)
        val checkButton: Button = findViewById(R.id.checkButton)
        val nextButton: Button = findViewById(R.id.nextButton)
        val backButton: Button = findViewById(R.id.backButton)

        loadNewSentence()

        checkButton.setOnClickListener {
            val userAnswer = answerInput.text.toString().trim()
            if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Wrong! The correct word was '$correctAnswer'.", Toast.LENGTH_SHORT).show()
            }
        }

        nextButton.setOnClickListener { loadNewSentence() }
        backButton.setOnClickListener { finish() }
    }

    private fun loadNewSentence() {
        val selectedLanguage = intent.getStringExtra("selectedLanguage") ?: "English"
        val sentenceData = dbHelper.getRandomFillBlankByLanguage(selectedLanguage)
        if (sentenceData != null) {
            val (sentence, missingWord) = sentenceData
            findViewById<TextView>(R.id.sentenceText).text = sentence.replace(missingWord, "_____")
            correctAnswer = missingWord
        } else {
            Toast.makeText(this, "No sentence data found!", Toast.LENGTH_SHORT).show()
        }
    }
}
