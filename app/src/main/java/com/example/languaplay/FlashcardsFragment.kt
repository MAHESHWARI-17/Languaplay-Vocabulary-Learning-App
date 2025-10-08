package com.example.languaplay

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FlashcardsFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var flashcardsAdapter: FlashcardsAdapter
    private var selectedLanguage = "English"  // Default language

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_flashcards, container, false)

        requireActivity().title = "Flash Cards"


        databaseHelper = DatabaseHelper(requireContext())
        recyclerView = view.findViewById(R.id.recyclerViewFlashcards)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Language Selection Buttons
        val btnSpanish: Button = view.findViewById(R.id.btnSpanish)
        val btnFrench: Button = view.findViewById(R.id.btnFrench)
        val btnTamil: Button = view.findViewById(R.id.btnTamil)
        val btnBack: Button = view.findViewById(R.id.btnBack)

        btnSpanish.setOnClickListener { loadFlashcards("Spanish") }
        btnFrench.setOnClickListener { loadFlashcards("French") }
        btnTamil.setOnClickListener { loadFlashcards("Tamil") }

        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Load Default Language Flashcards
        loadFlashcards(selectedLanguage)

        return view
    }

    private fun loadFlashcards(language: String) {
        selectedLanguage = language
        val flashcards = databaseHelper.getFlashcardsByLanguage(language, 10)
        flashcardsAdapter = FlashcardsAdapter(flashcards)
        recyclerView.adapter = flashcardsAdapter
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmation()
            }
        })
    }

    private fun showExitConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit Flashcards")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                findNavController().navigate(R.id.action_flashcardsFragment_to_homeFragment)
            }
            .setNegativeButton("No", null)
            .show()
    }
}

// RecyclerView Adapter for Flashcards
class FlashcardsAdapter(private val flashcards: List<Pair<String, String>>) :
    RecyclerView.Adapter<FlashcardsAdapter.FlashcardViewHolder>() {

    class FlashcardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordTextView: TextView = view.findViewById(R.id.textWord)
        val meaningTextView: TextView = view.findViewById(R.id.textMeaning)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flashcard, parent, false)
        return FlashcardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        val (word, meaning) = flashcards[position]
        holder.wordTextView.text = word
        holder.meaningTextView.text = meaning
    }

    override fun getItemCount(): Int = flashcards.size
}
