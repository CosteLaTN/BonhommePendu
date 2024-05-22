package com.example.myapplication

import org.junit.Assert.*
import org.junit.Test

class JeuTest {

    @Test
    fun testEssayerUneLettre() {
        val mots = arrayListOf("KOTLIN", "ANDROID", "MOBILE")
        val jeu = Jeu(mots)

        jeu.motADeviner = "KOTLIN"

        val resultK = jeu.tryLetter('K')
        assertEquals(listOf(0), resultK)
        assertEquals(1, jeu.pointage)
        assertEquals(0, jeu.nbErreurs)

        val resultO = jeu.tryLetter('O')
        assertEquals(listOf(1), resultO)
        assertEquals(2, jeu.pointage)
        assertEquals(0, jeu.nbErreurs)

        val resultZ = jeu.tryLetter('Z')
        assertTrue(resultZ.isEmpty())
        assertEquals(2, jeu.pointage)
        assertEquals(1, jeu.nbErreurs)
    }

    @Test
    fun testEstReussi() {
        val mots = arrayListOf("KOTLIN", "ANDROID", "MOBILE")
        val jeu = Jeu(mots)

        jeu.motADeviner = "KOTLIN"

        jeu.tryLetter('K')
        jeu.tryLetter('O')
        jeu.tryLetter('T')
        jeu.tryLetter('L')
        jeu.tryLetter('I')
        jeu.tryLetter('N')

        assertTrue(jeu.isPassed())

        val jeuPartial = Jeu(mots)
        jeuPartial.motADeviner = "KOTLIN"
        jeuPartial.tryLetter('K')
        assertFalse(jeuPartial.isPassed())
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEmptyWordsList() {
        Jeu(emptyList())
    }

    @Test
    fun testSingleLetterWord() {
        val mots = arrayListOf("A")
        val jeu = Jeu(mots)

        jeu.motADeviner = "A"

        val resultA = jeu.tryLetter('A')
        assertEquals(listOf(0), resultA)
        assertEquals(1, jeu.pointage)
        assertEquals(0, jeu.nbErreurs)
        assertTrue(jeu.isPassed())
    }

    @Test
    fun testSingleLetterWordIncorrectGuess() {
        val mots = arrayListOf("A")
        val jeu = Jeu(mots)

        jeu.motADeviner = "A"

        val resultB = jeu.tryLetter('B')
        assertTrue(resultB.isEmpty())
        assertEquals(0, jeu.pointage)
        assertEquals(1, jeu.nbErreurs)
        assertFalse(jeu.isPassed())
    }

    @Test
    fun testMaxErrors() {
        val mots = arrayListOf("TEST")
        val jeu = Jeu(mots)

        jeu.motADeviner = "TEST"

        jeu.tryLetter('A')
        jeu.tryLetter('B')
        jeu.tryLetter('C')
        jeu.tryLetter('D')
        jeu.tryLetter('F')
        jeu.tryLetter('G')

        assertEquals(6, jeu.nbErreurs)
        assertFalse(jeu.isPassed())
    }

    @Test
    fun testIgnoreCase() {
        val mots = arrayListOf("Test")
        val jeu = Jeu(mots)

        jeu.motADeviner = "Test"

        val resultT = jeu.tryLetter('t')
        assertEquals(listOf(0, 3), resultT)
        assertEquals(2, jeu.pointage)
        assertEquals(0, jeu.nbErreurs)
    }

    @Test
    fun testRepeatedLetters() {
        val mots = arrayListOf("BALLOON")
        val jeu = Jeu(mots)

        jeu.motADeviner = "BALLOON"

        val resultO = jeu.tryLetter('O')
        assertEquals(listOf(4, 5), resultO)
        assertEquals(2, jeu.pointage)
        assertEquals(0, jeu.nbErreurs)
    }


}
