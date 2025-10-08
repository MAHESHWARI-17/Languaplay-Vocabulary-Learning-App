package com.example.languaplay

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "LanguaPlay.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE words (id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT, meaning TEXT, language TEXT)")
        db.execSQL("CREATE TABLE sentences (id INTEGER PRIMARY KEY AUTOINCREMENT, sentence TEXT, missing_word TEXT, language TEXT)")
        db.execSQL("CREATE TABLE quiz (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, option1 TEXT, option2 TEXT, option3 TEXT, correct_answer TEXT, language TEXT)")

        debugDatabase(db)
        seedDatabase(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS words")
        db.execSQL("DROP TABLE IF EXISTS sentences")
        db.execSQL("DROP TABLE IF EXISTS quiz")
        onCreate(db)
    }

    private fun debugDatabase(db: SQLiteDatabase) {
        val cursor = db.rawQuery("SELECT COUNT(*) FROM words", null)
        if (cursor.moveToFirst()) {
            Log.d("DatabaseHelper", "Total words in DB: ${cursor.getInt(0)}")
        }
        cursor.close()
    }

    private fun seedDatabase(db: SQLiteDatabase) {
        insertWords(db)
        insertSentences(db)
        insertQuizQuestions(db)
    }

    private fun insertWords(db: SQLiteDatabase) {
        val words = listOf(
            Triple("Hello", "Hola", "Spanish"), Triple("Goodbye", "Adiós", "Spanish"),
            Triple("Thank you", "Gracias", "Spanish"), Triple("Please", "Por favor", "Spanish"),
            Triple("Yes", "Sí", "Spanish"), Triple("No", "No", "Spanish"),

            Triple("Hello", "Bonjour", "French"), Triple("Goodbye", "Au revoir", "French"),
            Triple("Thank you", "Merci", "French"), Triple("Please", "S'il vous plaît", "French"),
            Triple("Yes", "Oui", "French"), Triple("No", "Non", "French"),

            Triple("Hello", "வணக்கம்", "Tamil"), Triple("Goodbye", "போய்வாருங்கள்", "Tamil"),
            Triple("Thank you", "நன்றி", "Tamil"), Triple("Please", "தயவு செய்து", "Tamil"),
            Triple("Yes", "ஆம்", "Tamil"), Triple("No", "இல்லை", "Tamil")
        )

        for ((word, meaning, language) in words) {
            val values = ContentValues().apply {
                put("word", word)
                put("meaning", meaning)
                put("language", language)
            }
            db.insert("words", null, values)
        }
    }

    private fun insertSentences(db: SQLiteDatabase) {
        val cursor = db.rawQuery("SELECT word, meaning, language FROM words", null)
        val templateMap = mapOf(
            "Spanish" to "_____ means '%s'",
            "French" to "_____ signifie '%s'",
            "Tamil" to "'%s' என்பதற்கு _____"
        )

        while (cursor.moveToNext()) {
            val word = cursor.getString(0)
            val meaning = cursor.getString(1)
            val language = cursor.getString(2)
            val template = templateMap[language] ?: "_____ means '%s'"
            val sentence = String.format(template, word)

            val values = ContentValues().apply {
                put("sentence", sentence)
                put("missing_word", meaning)
                put("language", language)
            }
            db.insert("sentences", null, values)
        }
        cursor.close()
    }

//

    private fun insertQuizQuestions(db: SQLiteDatabase) {
        val cursor = db.rawQuery("SELECT word, meaning, language FROM words", null)

        while (cursor.moveToNext()) {
            val word = cursor.getString(0)           // English
            val meaning = cursor.getString(1)        // Translation
            val language = cursor.getString(2)

            val question: String
            val correctAnswer: String

            if (language == "Tamil") {
                // Flip for Tamil: ask "What is the Tamil word for 'EnglishWord'?"
                question = "What is the Tamil word for \"$word\"?"
                correctAnswer = meaning
            } else {
                // Normal: ask "What is 'TamilWord' in English?"
                question = "What is '$word' in $language?"
                correctAnswer = meaning
            }

            // Get 2 wrong options from same language but different meaning
            val optionsCursor = db.rawQuery(
                if (language == "Tamil") {
                    "SELECT meaning FROM words WHERE language = ? AND meaning != ? ORDER BY RANDOM() LIMIT 2"
                } else {
                    "SELECT meaning FROM words WHERE language = ? AND word != ? ORDER BY RANDOM() LIMIT 2"
                },
                if (language == "Tamil") arrayOf(language, correctAnswer) else arrayOf(language, word)
            )

            val options = mutableListOf<String>()
            while (optionsCursor.moveToNext()) {
                options.add(optionsCursor.getString(0))
            }
            optionsCursor.close()

            if (options.size < 2) continue

            options.add(correctAnswer)
            options.shuffle()

            val values = ContentValues().apply {
                put("question", question)
                put("option1", options[0])
                put("option2", options[1])
                put("option3", options[2])
                put("correct_answer", correctAnswer)
                put("language", language)
            }

            db.insert("quiz", null, values)
        }

        cursor.close()
    }



    fun getFlashcardsByLanguage(language: String, limit: Int = 10): List<Pair<String, String>> {
        val flashcards = mutableListOf<Pair<String, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT word, meaning FROM words WHERE language = ? ORDER BY RANDOM() LIMIT ?",
            arrayOf(language, limit.toString())
        )
        while (cursor.moveToNext()) {
            flashcards.add(Pair(cursor.getString(0), cursor.getString(1)))
        }
        cursor.close()
        return flashcards
    }

    fun getRandomQuizQuestionByLanguage(language: String): Triple<String, List<String>, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT question, option1, option2, option3, correct_answer FROM quiz WHERE language = ? ORDER BY RANDOM() LIMIT 1",
            arrayOf(language)
        )
        return if (cursor.moveToFirst()) {
            val question = cursor.getString(0)
            val options = listOf(cursor.getString(1), cursor.getString(2), cursor.getString(3))
            val correctAnswer = cursor.getString(4)
            cursor.close()
            Triple(question, options, correctAnswer)
        } else {
            cursor.close()
            null
        }
    }

    fun getRandomFillBlankByLanguage(language: String): Pair<String, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT sentence, missing_word FROM sentences WHERE language = ? ORDER BY RANDOM() LIMIT 1",
            arrayOf(language)
        )
        return if (cursor.moveToFirst()) {
            val sentence = cursor.getString(0)
            val missingWord = cursor.getString(1)
            cursor.close()
            Pair(sentence, missingWord)
        } else {
            cursor.close()
            null
        }
    }

    fun getRandomFlashcardWord(): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT word, meaning FROM words ORDER BY RANDOM() LIMIT 1", null)

        return if (cursor.moveToFirst()) {
            val word = cursor.getString(0)
            val meaning = cursor.getString(1)
            cursor.close()
            "$word - $meaning"
        } else {
            cursor.close()
            null
        }
    }

    fun checkTamilQuestions(): List<Triple<String, List<String>, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT question, option1, option2, option3, correct_answer FROM quiz WHERE language = 'Tamil'",
            null
        )
        val questions = mutableListOf<Triple<String, List<String>, String>>()
        while (cursor.moveToNext()) {
            val question = cursor.getString(0)
            val options = listOf(cursor.getString(1), cursor.getString(2), cursor.getString(3))
            val answer = cursor.getString(4)
            questions.add(Triple(question, options, answer))
        }
        cursor.close()
        return questions
    }
}




//package com.example.languaplay
//
//import android.content.ContentValues
//import android.content.Context
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//import android.database.Cursor
//import android.util.Log
//
//class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "LanguaPlay.db", null, 1) {
//
//    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL("CREATE TABLE words (id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT, meaning TEXT, language TEXT)")
//        db.execSQL("CREATE TABLE sentences (id INTEGER PRIMARY KEY AUTOINCREMENT, sentence TEXT, missing_word TEXT, language TEXT)")
//        db.execSQL("CREATE TABLE quiz (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, option1 TEXT, option2 TEXT, option3 TEXT, correct_answer TEXT, language TEXT)")
//
//        // ✅ Pass `db` instead of calling `getReadableDatabase()`
//        debugDatabase(db)
//        seedDatabase(db)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS words")
//        db.execSQL("DROP TABLE IF EXISTS sentences")
//        db.execSQL("DROP TABLE IF EXISTS quiz")
//        onCreate(db)
//    }
//
//    private fun debugDatabase(db: SQLiteDatabase) {
//        val cursor = db.rawQuery("SELECT COUNT(*) FROM words", null)
//        if (cursor.moveToFirst()) {
//            Log.d("DatabaseHelper", "Total words in DB: ${cursor.getInt(0)}")
//        }
//        cursor.close()
//    }
//
//    private fun seedDatabase(db: SQLiteDatabase) {
//        insertWords(db)
//        insertSentences(db)
//        insertQuizQuestions(db)
//    }
//
//    private fun insertWords(db: SQLiteDatabase) {
//        val words = listOf(
//            Triple("Hello", "Hola", "Spanish"), Triple("Goodbye", "Adiós", "Spanish"),
//            Triple("Thank you", "Gracias", "Spanish"), Triple("Please", "Por favor", "Spanish"),
//            Triple("Yes", "Sí", "Spanish"), Triple("No", "No", "Spanish"),
//            Triple("Hello", "Bonjour", "French"), Triple("Goodbye", "Au revoir", "French"),
//            Triple("Thank you", "Merci", "French"), Triple("Please", "S'il vous plaît", "French"),
//            Triple("Yes", "Oui", "French"), Triple("No", "Non", "French"),
//            Triple("Hello", "வணக்கம்", "Tamil"), Triple("Goodbye", "போய்வாருங்கள்", "Tamil"),
//            Triple("Thank you", "நன்றி", "Tamil"), Triple("Please", "தயவு செய்து", "Tamil"),
//            Triple("Yes", "ஆம்", "Tamil"), Triple("No", "இல்லை", "Tamil")
//        )
//        for ((word, meaning, language) in words) {
//            val values = ContentValues().apply {
//                put("word", word)
//                put("meaning", meaning)
//                put("language", language)
//            }
//            db.insert("words", null, values)
//        }
//    }
//
//    private fun insertSentences(db: SQLiteDatabase) {
//        val sentences = listOf(
//            Triple("_____ días", "Buenos", "Spanish"), Triple("Gracias, _____", "amigo", "Spanish"),
//            Triple("Adiós, nos _____", "vemos", "Spanish"),
//            Triple("Il est très _____", "intelligent", "French"), Triple("Merci, _____", "beaucoup", "French"),
//            Triple("Je m'appelle _____", "Jean", "French"),
//            Triple("நீங்கள் எப்படி _____?", "உள்ளீர்கள்", "Tamil"), Triple("நன்றி, _____", "நண்பா", "Tamil"),
//            Triple("வணக்கம், _____ உன்னுடைய பெயர் என்ன?", "உங்கள்", "Tamil")
//        )
//        for ((sentence, missingWord, language) in sentences) {
//            val values = ContentValues().apply {
//                put("sentence", sentence)
//                put("missing_word", missingWord)
//                put("language", language)
//            }
//            db.insert("sentences", null, values)
//        }
//    }
//
//    fun getFlashcardsByLanguage(language: String, limit: Int = 10): List<Pair<String, String>> {
//        val flashcards = mutableListOf<Pair<String, String>>()
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(
//            "SELECT word, meaning FROM words WHERE language = ? ORDER BY RANDOM() LIMIT ?",
//            arrayOf(language, limit.toString())
//        )
//
//        while (cursor.moveToNext()) {
//            flashcards.add(Pair(cursor.getString(0), cursor.getString(1)))
//        }
//        cursor.close()
//        return flashcards
//    }
//
//
//
//
//    private fun insertQuizQuestions(db: SQLiteDatabase) {
//        val quizData = listOf(
//            arrayOf("What is 'Hello' in Spanish?", "Hola", "Adiós", "Gracias", "Hola", "Spanish"),
//            arrayOf("What is 'Thank you' in Spanish?", "Por favor", "Sí", "Gracias", "Gracias", "Spanish"),
//            arrayOf("Comment dit-on 'Goodbye' en français?", "Bonjour", "Au revoir", "Merci", "Au revoir", "French"),
//            arrayOf("Que signifie 'Merci' en français?", "S'il vous plaît", "Merci", "Non", "Merci", "French"),
//            arrayOf("'வணக்கம்' என்றால் என்ன?", "Hello", "Goodbye", "Thank you", "Hello", "Tamil"),
//            arrayOf("'நன்றி' என்றால் என்ன?", "Hello", "Thank you", "Please", "Thank you", "Tamil")
//        )
//        for (question in quizData) {
//            val values = ContentValues().apply {
//                put("question", question[0])
//                put("option1", question[1])
//                put("option2", question[2])
//                put("option3", question[3])
//                put("correct_answer", question[4])
//                put("language", question[5])
//            }
//            db.insert("quiz", null, values)
//        }
//    }
//
//    fun getRandomQuizQuestionByLanguage(language: String): Triple<String, List<String>, String>? {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(
//            "SELECT question, option1, option2, option3, correct_answer FROM quiz WHERE language = ? ORDER BY RANDOM() LIMIT 1",
//            arrayOf(language)
//        )
//        return if (cursor.moveToFirst()) {
//            val question = cursor.getString(0)
//            val options = listOf(cursor.getString(1), cursor.getString(2), cursor.getString(3))
//            val correctAnswer = cursor.getString(4)
//            cursor.close()
//            Triple(question, options, correctAnswer)
//        } else {
//            cursor.close()
//            null
//        }
//    }
//
//
//
//
//    fun getRandomSentence(): Pair<String, String>? {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT sentence, missing_word FROM sentences ORDER BY RANDOM() LIMIT 1", null)
//        return if (cursor.moveToFirst()) {
//            val sentence = cursor.getString(0)
//            val missingWord = cursor.getString(1)
//            cursor.close()
//            Pair(sentence, missingWord)
//        } else {
//            cursor.close()
//            null
//        }
//    }
//    fun getRandomFillBlankByLanguage(language: String): Pair<String, String>? {
//        val db = readableDatabase
//        val cursor = db.rawQuery(
//            "SELECT sentence, missing_word FROM sentences WHERE language = ? ORDER BY RANDOM() LIMIT 1",
//            arrayOf(language)
//        )
//
//        return if (cursor.moveToFirst()) {
//            val sentence = cursor.getString(0)
//            val missingWord = cursor.getString(1)
//            cursor.close()
//            Pair(sentence, missingWord)
//        } else {
//            cursor.close()
//            null
//        }
//    }
//
//
//
//    fun getRandomQuizQuestion(): Triple<String, List<String>, String>? {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT question, option1, option2, option3, correct_answer FROM quiz ORDER BY RANDOM() LIMIT 1", null)
//        return if (cursor.moveToFirst()) {
//            val question = cursor.getString(0)
//            val options = listOf(cursor.getString(1), cursor.getString(2), cursor.getString(3))
//            val correctAnswer = cursor.getString(4)
//            cursor.close()
//            Triple(question, options, correctAnswer)
//        } else {
//            cursor.close()
//            null
//        }
//    }
//
//    fun getRandomFlashcardWord(): String? {
//        val db = readableDatabase
//        val cursor: Cursor = db.rawQuery("SELECT word, meaning FROM words ORDER BY RANDOM() LIMIT 1", null)
//
//        return if (cursor.moveToFirst()) {
//            val word = cursor.getString(0)
//            val meaning = cursor.getString(1)
//            cursor.close()
//            "$word - $meaning"
//        } else {
//            cursor.close()
//            null
//        }
//    }
//
//    fun checkTamilQuestions(): List<Triple<String, List<String>, String>> {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT question, option1, option2, option3, answer FROM quiz WHERE language = 'Tamil'", null)
//        val questions = mutableListOf<Triple<String, List<String>, String>>()
//
//        while (cursor.moveToNext()) {
//            val question = cursor.getString(0)
//            val options = listOf(cursor.getString(1), cursor.getString(2), cursor.getString(3))
//            val answer = cursor.getString(4)
//            questions.add(Triple(question, options, answer))
//        }
//
//        cursor.close()
//        db.close()
//        return questions
//    }
//
//
//
//}
//
//
//
