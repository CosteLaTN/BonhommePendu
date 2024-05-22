package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameHistoryAdapter(private val historyList: List<GameHistory>) :
    RecyclerView.Adapter<GameHistoryAdapter.GameHistoryViewHolder>() {

    class GameHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordTextView: TextView = itemView.findViewById(R.id.wordTextView)
        val difficultyTextView: TextView = itemView.findViewById(R.id.difficultyTextView)
        val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_game_history, parent, false)
        return GameHistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameHistoryViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.wordTextView.text = "Word: ${currentItem.word}"
        holder.difficultyTextView.text = "Difficulty: ${currentItem.difficulty}"
        holder.durationTextView.text = "Duration: ${currentItem.duration / 1000} sec"
        holder.dateTextView.text = "Date: ${java.text.DateFormat.getDateTimeInstance().format(currentItem.date)}"
    }

    override fun getItemCount() = historyList.size
}
