package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class HistoriqueActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: GameHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = DatabaseHelper(this)
        applyLocale() // Applique la langue sélectionnée
        setContentView(R.layout.activity_historique)

        historyRecyclerView = findViewById(R.id.recyclerviewHistorique)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        chargerHistorique()

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish() // Retour à l'activité précédente
        }
    }

    private fun applyLocale() {
        val selectedLanguage = databaseHelper.getPreference("language", "english") //Recuperes les preferences avec notre Methode getPreference
        val locale = when (selectedLanguage) {   //Appliques la "Locale en fonction de la langue séléctionné" (Vas chercher le bon string)
            "french" -> Locale("fr")
            else -> Locale("en")
        }
        Locale.setDefault(locale)
        val config = resources.configuration   //Vas chercher dans le file resources la bonne config et l'applique
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)     //https://stackoverflow.com/questions/6377195/proper-way-to-get-displaymetrics-getresources-or-getwindowmanager
    }

    private fun chargerHistorique() {
        val historyList = databaseHelper.getAllGameHistory()
        historyAdapter = GameHistoryAdapter(historyList)
        historyRecyclerView.adapter = historyAdapter //afficher l'historique dans le recycler
    }
}
