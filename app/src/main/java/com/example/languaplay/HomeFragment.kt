package com.example.languaplay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    private lateinit var rootView: View  // Hold the inflated view

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        rootView.setBackgroundResource(R.drawable.homepage_bg)

        val sharedPref = requireActivity().getSharedPreferences("LanguaPlay", Context.MODE_PRIVATE)
        val selectedLanguage = sharedPref.getString("selected_language", "English")

        rootView.findViewById<Button>(R.id.btnFlashcards).setOnClickListener {
            findNavController().navigate(R.id.flashcardsFragment)
        }

        rootView.findViewById<Button>(R.id.btnQuiz).setOnClickListener {
            findNavController().navigate(R.id.quizFragment)
        }

        rootView.findViewById<Button>(R.id.btnFillBlanks).setOnClickListener {
            findNavController().navigate(R.id.fillBlanksFragment)
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        updateScores()  // Always call this after view is initialized
    }

    private fun updateScores() {
        val sharedPref = requireActivity().getSharedPreferences("LanguaPlayScores", Context.MODE_PRIVATE)

        val quizScore = sharedPref.getInt("quiz_high_score", 0)
        val fillBlanksScore = sharedPref.getInt("fillblanks_high_score", 0)

        val quizScoreTextView = rootView.findViewById<TextView>(R.id.quizScoreTextView)
        val fillBlanksScoreTextView = rootView.findViewById<TextView>(R.id.fillBlanksScoreTextview)

        quizScoreTextView.text = "Quiz High Score: $quizScore"
        fillBlanksScoreTextView.text = "Fill in the Blanks High Score: $fillBlanksScore"
    }
}
