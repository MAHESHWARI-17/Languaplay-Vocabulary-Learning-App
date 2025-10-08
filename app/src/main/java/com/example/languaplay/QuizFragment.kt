package com.example.languaplay

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog

class QuizFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var languageSpinner: Spinner
    private lateinit var quizContainer: LinearLayout
    private lateinit var questionTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private var correctAnswer: String = ""
    private var selectedLanguage: String = ""
    private var score: Int = 0
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        requireActivity().title = "Quiz"

        dbHelper = DatabaseHelper(requireContext())
        sharedPref = requireActivity().getSharedPreferences("LanguaPlayScores", Context.MODE_PRIVATE)

        languageSpinner = view.findViewById(R.id.languageSpinner)
        quizContainer = view.findViewById(R.id.quizContainer)
        questionTextView = view.findViewById(R.id.questionTextView)
        optionsRadioGroup = view.findViewById(R.id.optionsRadioGroup)
        submitButton = view.findViewById(R.id.submitAnswerButton)
        scoreTextView = view.findViewById(R.id.quizScoreText)

        quizContainer.visibility = View.GONE

        val languages = listOf("Select Language", "English", "Tamil", "French", "Spanish")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
        languageSpinner.adapter = adapter

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var firstSelection = true

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (firstSelection) {
                    firstSelection = false
                    return
                }

                selectedLanguage = parent.getItemAtPosition(position).toString()

                if (selectedLanguage != "Select Language") {
                    quizContainer.visibility = View.VISIBLE
                    score = 0
                    updateScore()
                    loadNewQuestion()
                } else {
                    quizContainer.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        submitButton.setOnClickListener {
            checkAnswer()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmation()
            }
        })
    }

    private fun loadNewQuestion() {
        val quizData = dbHelper.getRandomQuizQuestionByLanguage(selectedLanguage)
        quizData?.let { (question, options, answer) ->
            questionTextView.text = question
            correctAnswer = answer

            optionsRadioGroup.removeAllViews()
            for (option in options) {
                val radioButton = RadioButton(requireContext())
                radioButton.text = option
                optionsRadioGroup.addView(radioButton)
            }
        } ?: run {
            questionTextView.text = "No questions available for $selectedLanguage"
        }
    }


    private fun checkAnswer() {
        val selectedId = optionsRadioGroup.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedOption = view?.findViewById<RadioButton>(selectedId)?.text.toString()
            if (selectedOption == correctAnswer) {
                score += 10
                Toast.makeText(requireContext(), "Correct!", Toast.LENGTH_SHORT).show()
                updateScore()
            } else {
                Toast.makeText(requireContext(), "Incorrect!", Toast.LENGTH_SHORT).show()
            }
            loadNewQuestion()
        } else {
            Toast.makeText(requireContext(), "Please select an answer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateScore() {
        scoreTextView.text = "Score: $score"

        val highScore = sharedPref.getInt("quiz_high_score", 0)
        if (score > highScore) {
            sharedPref.edit().putInt("quiz_high_score", score).apply()
        }
    }

    private fun showExitConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit Quiz")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                findNavController().navigate(R.id.action_quizFragment_to_homeFragment)
            }
            .setNegativeButton("No", null)
            .show()
    }
}



//package com.example.languaplay
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.view.*
//import android.widget.*
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import androidx.activity.OnBackPressedCallback
//import androidx.appcompat.app.AlertDialog
//
//class QuizFragment : Fragment() {
//
//    private lateinit var dbHelper: DatabaseHelper
//    private lateinit var languageSpinner: Spinner
//    private lateinit var quizContainer: LinearLayout
//    private lateinit var questionTextView: TextView
//    private lateinit var optionsRadioGroup: RadioGroup
//    private lateinit var submitButton: Button
//    private lateinit var scoreTextView: TextView
//
//    private var correctAnswer: String = ""
//    private var selectedLanguage: String = ""
//    private var score: Int = 0
//    private lateinit var sharedPref: SharedPreferences
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val view = inflater.inflate(R.layout.fragment_quiz, container, false)
//
//        requireActivity().title = "Quiz"
//
//        dbHelper = DatabaseHelper(requireContext())
//        sharedPref = requireActivity().getSharedPreferences("LanguaPlayPrefs", Context.MODE_PRIVATE)
//
//        languageSpinner = view.findViewById(R.id.languageSpinner)
//        quizContainer = view.findViewById(R.id.quizContainer)
//        questionTextView = view.findViewById(R.id.questionTextView)
//        optionsRadioGroup = view.findViewById(R.id.optionsRadioGroup)
//        submitButton = view.findViewById(R.id.submitAnswerButton)
//        scoreTextView = view.findViewById(R.id.quizScoreText)
//
//        quizContainer.visibility = View.GONE
//
//        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            var firstSelection = true
//
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                if (firstSelection) {
//                    firstSelection = false
//                    return
//                }
//                selectedLanguage = parent.getItemAtPosition(position).toString()
//                languageSpinner.visibility = View.GONE
//                quizContainer.visibility = View.VISIBLE
//                score = 0
//                updateScore()
//                loadNewQuestion()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }
//
//        submitButton.setOnClickListener {
//            checkAnswer()
//        }
//
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                showExitConfirmation()
//            }
//        })
//    }
//
//    private fun loadNewQuestion() {
//        val quizData = dbHelper.getRandomQuizQuestionByLanguage(selectedLanguage)
//        quizData?.let { (question, options, answer) ->
//            questionTextView.text = question
//            correctAnswer = answer
//
//            optionsRadioGroup.removeAllViews()
//            for (option in options) {
//                val radioButton = RadioButton(requireContext())
//                radioButton.text = option
//                optionsRadioGroup.addView(radioButton)
//            }
//        } ?: run {
//            questionTextView.text = "No questions available for $selectedLanguage"
//        }
//    }
//
//    private fun checkAnswer() {
//        val selectedId = optionsRadioGroup.checkedRadioButtonId
//        if (selectedId != -1) {
//            val selectedOption = view?.findViewById<RadioButton>(selectedId)?.text.toString()
//            if (selectedOption == correctAnswer) {
//                score += 10
//                Toast.makeText(requireContext(), "Correct!", Toast.LENGTH_SHORT).show()
//                updateScore()
//            } else {
//                Toast.makeText(requireContext(), "Incorrect!", Toast.LENGTH_SHORT).show()
//            }
//            loadNewQuestion()
//        } else {
//            Toast.makeText(requireContext(), "Please select an answer", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun updateScore() {
//        scoreTextView.text = "Score: $score"
//
//        val highScore = sharedPref.getInt("quiz_high_score", 0)
//        if (score > highScore) {
//            sharedPref.edit().putInt("quiz_high_score", score).apply()
//        }
//    }
//
//    private fun showExitConfirmation() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Exit Quiz")
//            .setMessage("Are you sure you want to exit?")
//            .setPositiveButton("Yes") { _, _ ->
//                findNavController().navigate(R.id.action_quizFragment_to_homeFragment)
//            }
//            .setNegativeButton("No", null)
//            .show()
//    }
//}
