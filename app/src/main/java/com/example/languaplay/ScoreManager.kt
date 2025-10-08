package com.example.languaplay

import android.content.Context
import android.content.SharedPreferences

object ScoreManager {
    private const val PREFS_NAME = "LanguaPlayScores"
    private const val FLASHCARDS_SCORE = "flashcards_high_score"
    private const val QUIZ_SCORE = "quiz_high_score"
    private const val FILL_SCORE = "fill_blanks_high_score"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getHighScore(context: Context, gameType: String): Int {
        val prefs = getPrefs(context)
        return when (gameType) {
            "flashcards" -> prefs.getInt(FLASHCARDS_SCORE, 0)
            "quiz" -> prefs.getInt(QUIZ_SCORE, 0)
            "fill" -> prefs.getInt(FILL_SCORE, 0)
            else -> 0
        }
    }

    fun setHighScore(context: Context, gameType: String, score: Int) {
        val prefs = getPrefs(context).edit()
        when (gameType) {
            "flashcards" -> prefs.putInt(FLASHCARDS_SCORE, score)
            "quiz" -> prefs.putInt(QUIZ_SCORE, score)
            "fill" -> prefs.putInt(FILL_SCORE, score)
        }
        prefs.apply()
    }
}
