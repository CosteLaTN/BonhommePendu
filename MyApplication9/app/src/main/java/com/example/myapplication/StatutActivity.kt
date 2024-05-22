package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StatutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statut)

        val message = intent.getStringExtra("MESSAGE")
        findViewById<TextView>(R.id.messageTextView).text = message

        val buttonRejouer = findViewById<Button>(R.id.rejouerButton)
        buttonRejouer.setOnClickListener {
            val intent = Intent(this, PenduActivity::class.java)
            intent.putExtra("RESET_GAME", true)
            startActivity(intent)
            finish()
        }
    }



}
