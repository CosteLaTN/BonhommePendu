package com.example.myapplication

public class Jeu(private val mots: List<String>) {
    var pointage: Int = 0
        private set

    var nbErreurs: Int = 0
        private set

    var motADeviner: String = ""

    val lettresCorrectes = mutableSetOf<Int>()

    init {
        require(mots.isNotEmpty()) { "La liste de mots a deviner ne doit pas être vide" }
        motADeviner = mots.random()
    }

    fun tryLetter(lettre: Char): List<Int> {
        val indicesTrouves = mutableListOf<Int>()

        for (i in motADeviner.indices) {
            val char = motADeviner[i]
            if (char.equals(lettre, ignoreCase = true) && i !in lettresCorrectes) {
                indicesTrouves.add(i)
                lettresCorrectes.add(i)
            }
        }

        if (indicesTrouves.isEmpty()) {
            nbErreurs++
        } else {
            pointage = lettresCorrectes.size
        }

        return indicesTrouves
    }

    fun isPassed(): Boolean {
        return motADeviner.indices.all { index ->
            !motADeviner[index].isLetter() || lettresCorrectes.contains(index)
        }
    }
}
