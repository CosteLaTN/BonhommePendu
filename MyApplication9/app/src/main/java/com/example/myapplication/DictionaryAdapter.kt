package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DictionaryAdapter(private val wordList: MutableList<String>) : RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder>() {

    private lateinit var databaseHelper: DatabaseHelper

    class DictionaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordTextView: TextView = itemView.findViewById(R.id.wordTextView)
        val deleteButton: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        databaseHelper = DatabaseHelper(parent.context)   //Fournit basicly l'état actuel (contexte) du ViewGroup parent qui contient justement notre recycler view
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemword, parent, false) //LayoutInflater convertit le fichier xml en vue
        return DictionaryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) { //Placer les mots dans leur positions adéquates en fonction de la position recue en parametre
        val currentItem = wordList[position]
        holder.wordTextView.text = currentItem
        holder.deleteButton.setOnClickListener { //Assigne le onclick au bouton pour actualy supprimer le mot dans la database
            databaseHelper.deleteWord(currentItem)
            removeWord(currentItem) //Si on n'utilise pas cette méthode le mot va etre supprimé dans la db mais pas au niveau de l'affichage
        }
    }

    override fun getItemCount() = wordList.size

    fun addWord(word: String) {
        wordList.add(word)
        notifyItemInserted(wordList.size - 1) //Calcule l'index du nouvel élément en fonction d'il y a combien d'éléments présent dans la liste (Avertit l'adapteur)
    }

    fun removeWord(word: String) {
        val position = wordList.indexOf(word)
        if (position >= 0) {
            wordList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
