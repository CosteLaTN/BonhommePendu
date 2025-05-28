package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class PreferencesActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var languageGroup: RadioGroup
    private lateinit var difficultyGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = DatabaseHelper(this)
        applyLocale() // Applique la langue sélectionnée a l'aide de la methode applyLocale
        setContentView(R.layout.activity_preferences)

        languageGroup = findViewById(R.id.languageGroup)
        difficultyGroup = findViewById(R.id.difficultyGroup)

        loadPreferences()

        findViewById<Button>(R.id.btnSavePreferences).setOnClickListener {
            sauvegarderPreference()
            updateLocale()
        }

        findViewById<Button>(R.id.btnOpenDictionary).setOnClickListener {
            startActivity(Intent(this, DictionnaireActivity::class.java))
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun applyLocale() {
        val selectedLanguage = databaseHelper.getPreference("language", "english")
        val locale = when (selectedLanguage) {
            "french" -> Locale("fr")
            else -> Locale("en")
        }
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun loadPreferences() {
        val savedLanguage = databaseHelper.getPreference("language", "english")
        val savedDifficulty = databaseHelper.getPreference("difficulty", "easy")

        when (savedLanguage) {
            "english" -> languageGroup.check(R.id.radioEnglish)
            "french" -> languageGroup.check(R.id.radioFrench)
        }

        when (savedDifficulty) {
            "easy" -> difficultyGroup.check(R.id.radioEasy)
            "medium" -> difficultyGroup.check(R.id.radioMedium)
            "hard" -> difficultyGroup.check(R.id.radioHard)
        }
    }

    private fun sauvegarderPreference() {
        val langueSelectionner = when (languageGroup.checkedRadioButtonId) {
            R.id.radioEnglish -> "english"
            R.id.radioFrench -> "french"
            else -> "english"
        }

        val difficulteSelectionner = when (difficultyGroup.checkedRadioButtonId) {
            R.id.radioEasy -> "easy"
            R.id.radioMedium -> "medium"
            R.id.radioHard -> "hard"
            else -> "easy"
        }

        databaseHelper.savePreference("language", langueSelectionner)                //Save les preferences en fonctions de quels radio button sont selected
        databaseHelper.savePreference("difficulty", difficulteSelectionner)

        Toast.makeText(this, getString(R.string.preferences_saved), Toast.LENGTH_SHORT).show()

        // Recrée l'activité principale pour appliquer les changements en fonction des préférences
        val intent = Intent(this, PenduActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateLocale() {
        applyLocale()
        recreate()// basically un refresh
    }
}
