package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AcceuilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceuil)

        //Création des Listeners pour les boutons "Démarrer le jeu", "Préférences" et "Historique"
        findViewById<Button>(R.id.btnStartGame).setOnClickListener {
            startActivity(Intent(this, PenduActivity::class.java))
        }


        findViewById<Button>(R.id.btnPreferences).setOnClickListener {
            startActivity(Intent(this, PreferencesActivity::class.java))
        }


        findViewById<Button>(R.id.btnHistory).setOnClickListener {
            startActivity(Intent(this, HistoriqueActivity::class.java))
        }
    }
}
