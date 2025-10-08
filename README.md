🧠 LanguaPlay – Interactive Vocabulary Learning App

LanguaPlay is an engaging Android application designed to make vocabulary learning fun, interactive, and effective. Through gamified learning experiences like Flashcards,Quizzes,and Fill in the Blanks, 
users can build and test their vocabulary while tracking progress and earning scores.

----------------------------------------------------
Platform

* Technology: Android (Native)
* Language: Kotlin
* UI Design: XML Layouts, Fragments, and Navigation Component
* IDE: Android Studio

----------------------------------------------------

Features

 Educational Games (Fragment-Based)

Each game is implemented as a separate **Fragment** for modularity and smooth navigation using the **Navigation Component**.

1. Flashcards

   * Swipe through vocabulary cards displaying words and meanings.
   * Visually engaging gradient backgrounds.
   * Data sourced from SQLite.

2. Quiz

   * Multiple-choice format with instant feedback.
   * Dynamic score tracking via `SharedPreferences`.
   * Simple UI built using `RadioGroup` and `RadioButtons`.

3. Fill in the Blanks

   * Contextual grammar game where users select the correct word to complete a sentence.
   * Reward-based scoring system.
   * Sentences and answers stored in SQLite.

-----------------------------------------------

Language Support

* Users can choose between **English**, **Tamil**, and **French**.
* Language preference stored using `SharedPreferences`.
* All word lists and questions are fetched from SQLite tables based on language selection.

-----------------------------------------------

Architecture & Data

Local Storage

* **SQLite (via SQLiteOpenHelper)**

  * Stores user accounts, vocabulary words, quizzes, and fill-in-the-blank templates.
  * Tables include:

    * `users` – user credentials
    * `words` – vocabulary and meanings
    * `quiz` – questions and correct answers
    * `sentences` – fill-in-the-blank data

Local State

* **SharedPreferences**

  * Stores:

    * User login state (`isLoggedIn`)
    * Selected language
    * Game high scores

---------------------------------------------

Multimedia Integration

* **MediaPlayer:** Background music for immersive gameplay.
* **SoundPool:** For fast sound effects during game interactions.

--------------------------------------------
UI/UX Design

* Consistent visual themes per screen:

  * Flashcards – Vibrant and colorful
  * Quiz – Bright and energetic
  * Fill in the Blanks – Soft pastel tones
* Modern design with gradient buttons, rounded corners, and vector icons.
* Smooth animations between screens and on button clicks.

--------------------------------------------

Tools & Technologies

| Component              | Technology Used              |
| ---------------------- | ---------------------------- |
| **Frontend**           | Kotlin, XML                  |
| **Backend / Data**     | SQLite                       |
| **Navigation**         | Android Navigation Component |
| **Sound & Media**      | MediaPlayer, SoundPool       |
| **Session Management** | SharedPreferences            |

-------------------------------------------

 Project Structure

```
com.example.languaplay/
│
├── database/
│   ├── UserDatabaseHelper.kt   # Handles user authentication
│   └── DatabaseHelper.kt       # Manages vocabulary, quiz & sentence data
│
├── ui/
│   ├── FlashcardsFragment.kt
│   ├── QuizFragment.kt
│   ├── FillBlanksFragment.kt
│   └── HomeFragment.kt
│
├── LoginActivity.kt
├── RegisterActivity.kt
├── MainActivity.kt
├── SplashActivity.kt
├── MusicPlayer.kt
├── ScoreManager.kt
└── res/
    ├── layout/
    ├── drawable/
    ├── menu/
    └── navigation/
```

------------------------------------------

###  Developed By

* M. Maheshwari (22I329)

---



This project is developed for academic purposes as part of the **Mobile Application Development (MAD)** course.
------------------------------------------
