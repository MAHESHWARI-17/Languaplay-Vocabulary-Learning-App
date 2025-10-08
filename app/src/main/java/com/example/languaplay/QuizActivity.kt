package com.example.languaplay

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var correctAnswer: String = ""
    private var score = 0
    private lateinit var sharedPref: SharedPreferences
    private lateinit var questionText: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var correctSound: MediaPlayer
    private lateinit var wrongSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        dbHelper = DatabaseHelper(this)
        sharedPref = getSharedPreferences("LanguaPlayPrefs", MODE_PRIVATE)

        questionText = findViewById(R.id.questionText)
        scoreTextView = findViewById(R.id.quizScoreText)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)

        correctSound = MediaPlayer.create(this, R.raw.correct_sound)
        wrongSound = MediaPlayer.create(this, R.raw.wrong_sound)

        updateScore()
        loadNewQuestion()

        option1.setOnClickListener { checkAnswer(option1) }
        option2.setOnClickListener { checkAnswer(option2) }
        option3.setOnClickListener { checkAnswer(option3) }
    }

    private fun loadNewQuestion() {
        val selectedLanguage = intent.getStringExtra("selectedLanguage") ?: "English"
        val quizQuestion = dbHelper.getRandomQuizQuestionByLanguage(selectedLanguage)
        if (quizQuestion != null) {
            val (question, options, answer) = quizQuestion
            questionText.text = question
            option1.text = options[0]
            option2.text = options[1]
            option3.text = options[2]
            correctAnswer = answer
        } else {
            Toast.makeText(this, "No quiz data found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAnswer(selectedButton: Button) {
        if (selectedButton.text.toString() == correctAnswer) {
            score += 10
            correctSound.start()
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            updateScore()
        } else {
            wrongSound.start()
            Toast.makeText(this, "Wrong! Try again.", Toast.LENGTH_SHORT).show()
        }
        loadNewQuestion()
    }

    private fun updateScore() {
        scoreTextView.text = "Score: $score"
        val highScore = sharedPref.getInt("quiz_high_score", 0)
        if (score > highScore) {
            sharedPref.edit().putInt("quiz_high_score", score).apply()
        }
    }
}
