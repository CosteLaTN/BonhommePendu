package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DictionnaireActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var wordEditText: EditText
    private lateinit var difficultySpinner: Spinner
    private lateinit var languageSpinner: Spinner
    private lateinit var dictionaryRecyclerView: RecyclerView
    private lateinit var dictionaryAdapter: DictionaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionnaire)

        databaseHelper = DatabaseHelper(this)

        wordEditText = findViewById(R.id.wordEditText)
        difficultySpinner = findViewById(R.id.difficultySpinner)
        languageSpinner = findViewById(R.id.languageSpinner)

        dictionaryRecyclerView = findViewById(R.id.recyclerviewDictionnaire)

        setupSpinner() //Call la méthode qui initialise le drop down menu pour l'affichage de choix de langue/difficulté
        setupRecyclerView()

        findViewById<Button>(R.id.addWordButton).setOnClickListener {
            ajouterMot()
        }



        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish() // Permet de revenir a l'activité précédente lorsque le bouton est cliqué
        }
    }

    private fun setupSpinner() {
        val difficulties = resources.getStringArray(R.array.difficulties) //recupere les niveaux de difficulté sous forme de tableau de strings
        val difficultyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficulties)
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) //defini le type de layout pour le menu dropdown
        difficultySpinner.adapter = difficultyAdapter //rempli le dropdown avec les éléments (niveau de difficulté)

        val languages = resources.getStringArray(R.array.languages)
        val languageAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = languageAdapter

        loadWords()
    }

    private fun setupRecyclerView() {
        dictionaryRecyclerView.layoutManager = LinearLayoutManager(this) //defini le type de layout ppour le recyclerview
        loadWords()
    }

    private fun loadWords() {
        val selectedLanguage = languageSpinner.selectedItem.toString()
        val selectedDifficulty = difficultySpinner.selectedItem.toString()
        val words = databaseHelper.getAllWords(selectedLanguage, selectedDifficulty).toMutableList()
        dictionaryAdapter = DictionaryAdapter(words) // Créer un adaptateur avec la liste des mots
        dictionaryRecyclerView.adapter = dictionaryAdapter // Associer l'adaptateur au RecyclerView
    }


    private fun ajouterMot() {
        val word = wordEditText.text.toString()
        val difficulty = difficultySpinner.selectedItem.toString()
        val language = languageSpinner.selectedItem.toString()

        if (word.isNotEmpty()) {
            val ligne = databaseHelper.insertWord(word, difficulty, language)
            if (ligne > 0) { // Si l'insertion a réussi
                Toast.makeText(this, getString(R.string.word_added), Toast.LENGTH_SHORT).show()
                wordEditText.text.clear() // Effacer le champ de saisie
                dictionaryAdapter.addWord(word) // Ajouter le mot à l'adaptateur
                loadWords() // Recharger les mots dans le RecyclerView
            } else {
                Toast.makeText(this, getString(R.string.word_not_added), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, getString(R.string.enter_word), Toast.LENGTH_SHORT).show()
        }
    }


}
