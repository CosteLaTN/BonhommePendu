package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class PenduActivity : AppCompatActivity() {

    private lateinit var jeu: Jeu
    private lateinit var motTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var imageJeu: ImageView
    private lateinit var databaseHelper: DatabaseHelper
    private var startTime: Long = 0
    private lateinit var difficulty: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        motTextView = findViewById(R.id.MotGuess)
        scoreTextView = findViewById(R.id.Score)
        imageJeu = findViewById(R.id.ImageJeu)
        databaseHelper = DatabaseHelper(this)

        // Liste de tous les boutons de lettres avec une array
        val letterButtons = arrayOf(
            R.id.btnA, R.id.btnB, R.id.btnC, R.id.btnD, R.id.btnE, R.id.btnF,
            R.id.btnG, R.id.btnH, R.id.btnI, R.id.btnJ, R.id.btnK, R.id.btnL,
            R.id.btnM, R.id.btnN, R.id.btnO, R.id.btnP, R.id.btnQ, R.id.btnR,
            R.id.btnS, R.id.btnT, R.id.btnU, R.id.btnV, R.id.btnW, R.id.btnX,
            R.id.btnY, R.id.btnZ
        )

        // Ajout de listeners a l'aide de l'id et traverse la liste complete de tous nos boutons
        for (buttonId in letterButtons) {
            findViewById<Button>(buttonId).setOnClickListener { clickedButton ->
                devinerLettre(clickedButton)
            }
        }

        // Listener pour le bouton de start
        findViewById<Button>(R.id.btnStart).setOnClickListener {
            confirmerRedemarrer()
        }

        // Listener pour le bouton de preference
        findViewById<Button>(R.id.btnPreferences).setOnClickListener {
            val intent = Intent(this, PreferencesActivity::class.java)
            startActivity(intent)
        }

        // Listener pour le bouton de l'historique
        findViewById<Button>(R.id.btnHistory).setOnClickListener {
            val intent = Intent(this, HistoriqueActivity::class.java)
            startActivity(intent)
        }

        // Vérifie si le jeu doit être réinitialisé
        val devraitEtreReinisialiser = intent.getBooleanExtra("RESET_GAME", false)
        if (devraitEtreReinisialiser) {
            resetJeu()
        } else {
            initialiserJeu()
        }
    }

    // Fonction pour deviner une lettre
    private fun devinerLettre(v: View) {
        val lettre = (v as Button).text[0]

        val indices = jeu.tryLetter(lettre)

        if (indices.isNotEmpty()) {
            Toast.makeText(this, "Lettre trouvée!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Mauvaise lettre!", Toast.LENGTH_SHORT).show()
        }

        // Désactive le bouton après chaque utilisation
        v.isEnabled = false
        updateUI()

        // Vérifie si le jeu est gagné ou perdu
        if (jeu.isPassed()) {
            val duration = SystemClock.elapsedRealtime() - startTime                                                //Nous permet de faire le calcul de durée de la partie on faisant la soustraction
            val gameHistory = GameHistory(0, jeu.motADeviner, difficulty, duration, System.currentTimeMillis())  //https://stackoverflow.com/questions/2978452/when-will-system-currenttimemillis-overflow
            databaseHelper.insertGameHistory(gameHistory)
            afficherStatut("GGWP!")
        } else if (jeu.nbErreurs >= 6) {
            val duration = SystemClock.elapsedRealtime() - startTime
            val gameHistory = GameHistory(0, jeu.motADeviner, difficulty, duration, System.currentTimeMillis())
            databaseHelper.insertGameHistory(gameHistory)
            afficherStatut("BIG L - le mot était: ${jeu.motADeviner}")
        }
    }

    // Affiche une boîte de dialogue pour confirmer le redémarrage
    private fun confirmerRedemarrer() {
        AlertDialog.Builder(this)
            .setTitle("Confirmer le reset")
            .setMessage("Voulez-vous vraiment restart la game ?")
            .setPositiveButton("Oui") { _, _ ->  //Ici on ignore le parametres car ce ne n'est pas nécessaire pour cette simple tache https://stackoverflow.com/questions/70757388/alertdialog-in-android-studio-kotlin-not-displaying
                resetJeu()
            }
            .setNegativeButton("Non", null)
            .show()
    }

    // Réinitialise le jeu
    private fun resetJeu() {
        val mots = arrayListOf("ANDROID", "KOTLIN", "MOBILE")
        difficulty = databaseHelper.getPreference("difficulty", "easy")
        jeu = Jeu(mots)
        startTime = SystemClock.elapsedRealtime() //On attribue le real time de l'ordinateur a starttime

        // Réactive tous les boutons de lettres
        val letterButtons = arrayOf(
            R.id.btnA, R.id.btnB, R.id.btnC, R.id.btnD, R.id.btnE, R.id.btnF,
            R.id.btnG, R.id.btnH, R.id.btnI, R.id.btnJ, R.id.btnK, R.id.btnL,
            R.id.btnM, R.id.btnN, R.id.btnO, R.id.btnP, R.id.btnQ, R.id.btnR,
            R.id.btnS, R.id.btnT, R.id.btnU, R.id.btnV, R.id.btnW, R.id.btnX,
            R.id.btnY, R.id.btnZ
        )

        for (buttonId in letterButtons) {
            findViewById<Button>(buttonId).isEnabled = true
        }

        updateUI()
    }

    // Initialise le jeu avec la liste et la difficulté avec le jeu contenant la liste de mots
    private fun initialiserJeu() {
        val mots = arrayListOf("ANDROID", "KOTLIN", "MOBILE")
        difficulty = databaseHelper.getPreference("difficulty", "easy")
        jeu = Jeu(mots)
        startTime = SystemClock.elapsedRealtime()

        updateUI()
    }

    // Met à jour l'interface utilisateur
    private fun updateUI() {
        val motCache = StringBuilder()
        for (i in jeu.motADeviner.indices) {
            if (jeu.lettresCorrectes.contains(i)) {
                motCache.append(jeu.motADeviner[i])
            } else {
                motCache.append('*')
            }
        }
        motTextView.text = motCache.toString()
        scoreTextView.text = "${jeu.pointage}"

        // Met à jour l'image en fonction du nombre d'erreurs
        val erreurImageId = when (jeu.nbErreurs) {
            0 -> R.drawable.acceuil
            1 -> R.drawable.err01
            2 -> R.drawable.err02
            3 -> R.drawable.err03
            4 -> R.drawable.err04
            5 -> R.drawable.err05
            else -> R.drawable.err06
        }
        imageJeu.setImageResource(erreurImageId)

        if (jeu.isPassed()) {
            afficherStatut("GGWP!")
        } else if (jeu.nbErreurs >= 6) {
            afficherStatut("BIG L - le mot était: ${jeu.motADeviner}")
        }
    }

    // Affiche le statut du jeu (gagné ou perdu)
    private fun afficherStatut(message: String) {
        val intent = Intent(this, StatutActivity::class.java)
        intent.putExtra("MESSAGE", message)
        startActivity(intent)
    }
}
