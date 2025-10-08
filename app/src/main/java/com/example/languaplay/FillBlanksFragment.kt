package com.example.languaplay

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import android.content.Context

class FillBlanksFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var languageSpinner: Spinner
    private lateinit var fillContainer: LinearLayout
    private lateinit var sentenceTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var scoreTextView: TextView

    private var correctWord: String = ""
    private var selectedLanguage: String = ""
    private var currentScore: Int = 0
    private var highestScore: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_fillblanks, container, false)

        dbHelper = DatabaseHelper(requireContext())

        // UI elements
        languageSpinner = view.findViewById(R.id.languageSpinner)
        fillContainer = view.findViewById(R.id.fillBlanksContainer)
        sentenceTextView = view.findViewById(R.id.fillBlanksSentence)
        answerEditText = view.findViewById(R.id.fillBlanksAnswer)
        submitButton = view.findViewById(R.id.fillBlanksSubmit)
        scoreTextView = view.findViewById(R.id.fillBlanksScore)

        fillContainer.visibility = View.GONE

        // ✅ Add items to the Spinner
        val languages = listOf("Select Language", "English", "Tamil", "French", "Spanish")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        // ✅ Spinner handling
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var firstSelection = true
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (firstSelection) {
                    firstSelection = false
                    return
                }

                selectedLanguage = parent.getItemAtPosition(position).toString()
                if (selectedLanguage != "Select Language") {
                    fillContainer.visibility = View.VISIBLE
                    loadNewSentence()
                } else {
                    fillContainer.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Load highest score from SharedPreferences
        highestScore = loadHighestScore()
        updateScoreUI()

        // Answer submit handling
        submitButton.setOnClickListener {
            val userAnswer = answerEditText.text.toString().trim()
            if (userAnswer.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your answer", Toast.LENGTH_SHORT).show()
            } else if (userAnswer.equals(correctWord, ignoreCase = true)) {
                Toast.makeText(requireContext(), "Correct!", Toast.LENGTH_SHORT).show()
                currentScore += 10
                answerEditText.text.clear()
                loadNewSentence()
            } else {
                Toast.makeText(requireContext(), "Incorrect. Try again.", Toast.LENGTH_SHORT).show()
                if (currentScore > highestScore) {
                    highestScore = currentScore
                    saveHighestScore(highestScore)
                }
                currentScore = 0 // Reset score if the answer is wrong
            }
            updateScoreUI()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Back press confirmation dialog
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmation()
            }
        })
    }

    private fun loadNewSentence() {
        val sentencePair = dbHelper.getRandomFillBlankByLanguage(selectedLanguage)
        if (sentencePair != null) {
            sentenceTextView.text = sentencePair.first
            correctWord = sentencePair.second
        } else {
            sentenceTextView.text = "No questions available for $selectedLanguage"
        }
    }

    private fun updateScoreUI() {
        scoreTextView.text = "Score: $currentScore"
    }

    private fun saveHighestScore(score: Int) {
        val sharedPreferences = requireActivity().getSharedPreferences("LanguaPlayScores", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("FillBlanksHighScore", score)
            apply()
        }
    }

    private fun loadHighestScore(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences("LanguaPlayScores", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("FillBlanksHighScore", 0)
    }

    private fun showExitConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit Fill in the Blanks")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                findNavController().navigate(R.id.action_fillBlanksFragment_to_homeFragment)
            }
            .setNegativeButton("No", null)
            .show()
    }
}
