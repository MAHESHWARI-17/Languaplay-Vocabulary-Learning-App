package com.example.languaplay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LanguageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_language_selection, container, false)

        view.findViewById<Button>(R.id.btnEnglish).setOnClickListener { selectLanguage("English") }
        view.findViewById<Button>(R.id.btnTamil).setOnClickListener { selectLanguage("Tamil") }
        view.findViewById<Button>(R.id.btnFrench).setOnClickListener { selectLanguage("French") }

        return view
    }

    private fun selectLanguage(language: String) {
        val sharedPref = requireActivity().getSharedPreferences("LanguaPlay", Context.MODE_PRIVATE)
        sharedPref.edit().putString("selected_language", language).apply()
        findNavController().navigate(R.id.homeFragment)
    }
}
