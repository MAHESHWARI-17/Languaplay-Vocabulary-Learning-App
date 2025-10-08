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
Screenshots
<img width="450" height="768" alt="image" src="https://github.com/user-attachments/assets/5aa7d9eb-c11e-48bd-bb60-37914003ef7a" />
<img width="450" height="764" alt="image" src="https://github.com/user-attachments/assets/2e97987b-9663-42f1-9b42-e8f4b8ad7453" />
<img width="421" height="723" alt="image" src="https://github.com/user-attachments/assets/af2f2364-2a0e-4a87-a71f-ca618c14f42b" />
<img width="441" height="717" alt="image" src="https://github.com/user-attachments/assets/79db1e42-fd38-42f9-b7e8-0c1bc558a36f" />
<img width="414" height="690" alt="image" src="https://github.com/user-attachments/assets/e2db565c-36a6-47df-bd48-dd27db860bf1" />
<img width="425" height="829" alt="image" src="https://github.com/user-attachments/assets/ad5be28b-0dcb-406e-80a6-49962639732b" />
<img width="392" height="676" alt="image" src="https://github.com/user-attachments/assets/ebb2ef9f-f498-43d2-a04e-9e12fb3f4566" />
<img width="412" height="749" alt="image" src="https://github.com/user-attachments/assets/85706980-a521-486c-91e3-d5a4b6a0c68e" />
<img width="408" height="748" alt="image" src="https://github.com/user-attachments/assets/6fb7fa49-1c0b-4777-aeb6-54a37d8d4715" />
<img width="437" height="646" alt="image" src="https://github.com/user-attachments/assets/b07aab17-5366-4a3b-b4d3-025d7116c051" />
<img width="441" height="748" alt="image" src="https://github.com/user-attachments/assets/1de462d3-7877-4ec8-a06b-94bc94c248cd" />


###  Developed By

* M. Maheshwari

---



This project is developed for academic purposes as part of the **Mobile Application Development (MAD)** course.
------------------------------------------
