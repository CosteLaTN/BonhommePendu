package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "game_history.db"
        private const val DATABASE_VERSION = 6
        private const val TABLE_NAME = "game_history"
        private const val COLUMN_ID = "id"
        private const val COLUMN_WORD = "word"
        private const val COLUMN_DIFFICULTY = "difficulty"
        private const val COLUMN_DURATION = "duration"
        private const val COLUMN_DATE = "date"
        private const val TABLE_DICTIONARY = "dictionary"
        private const val COLUMN_WORD_TEXT = "word"
        private const val COLUMN_WORD_DIFFICULTY = "difficulty"
        private const val COLUMN_WORD_LANGUAGE = "language"
        private const val TABLE_PREFERENCES = "preferences"
        private const val COLUMN_KEY = "key"
        private const val COLUMN_VALUE = "value"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Création de toutes nos tables
        val createGameHistoryTable = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_WORD + " TEXT,"
                + COLUMN_DIFFICULTY + " TEXT,"
                + COLUMN_DURATION + " INTEGER,"
                + COLUMN_DATE + " INTEGER" + ")")


        val createDictionaryTable = ("CREATE TABLE " + TABLE_DICTIONARY + "("
                + COLUMN_WORD_TEXT + " TEXT PRIMARY KEY,"
                + COLUMN_WORD_DIFFICULTY + " TEXT,"
                + COLUMN_WORD_LANGUAGE + " TEXT" + ")")


        val createPreferencesTable = ("CREATE TABLE " + TABLE_PREFERENCES + "("
                + COLUMN_KEY + " TEXT PRIMARY KEY,"
                + COLUMN_VALUE + " TEXT" + ")")

        db.execSQL(createGameHistoryTable)
        db.execSQL(createDictionaryTable)
        db.execSQL(createPreferencesTable)
    }
   //A chaque fois que la db est mise-a-jour, efface les données
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DICTIONARY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PREFERENCES")
        onCreate(db)
    }

    // S'occupe de l'insertion de chaque game dans notre db d'historique de partie
    fun insertGameHistory(gameHistory: GameHistory): Long {
        val db = this.writableDatabase //Rend la db modifiable
        val values = ContentValues()   //Objet nous permettant de stocker les valeurs
        values.put(COLUMN_WORD, gameHistory.word)
        values.put(COLUMN_DIFFICULTY, gameHistory.difficulty)
        values.put(COLUMN_DURATION, gameHistory.duration)
        values.put(COLUMN_DATE, gameHistory.date)

        val success = db.insert(TABLE_NAME, null, values) //Insere les valeurs selon la table, et ignore les colonnes vides en attribuant null
        db.close()
        return success
    }

    // Récupère toutes les parties jouées a partir de la liste
    fun getAllGameHistory(): List<GameHistory> {
        val historyList = ArrayList<GameHistory>()
        val selectQuery = "SELECT  * FROM $TABLE_NAME ORDER BY $COLUMN_DATE DESC"
        val db = this.readableDatabase
        var cursor: Cursor? = null


            cursor = db.rawQuery(selectQuery, null)  //Éxécute la requete sql qui est mentionnée en haut et retourne le résultat dans cursor


        if (cursor.moveToFirst()) {  //Vérifie si il y a une valeur dans cursor, si oui exécute le code en bas
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))       //Va chercher chacune des valeurs et le met dans la variable respective
                val word = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORD))
                val difficulty = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY))
                val duration = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DURATION))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE))

                val gameHistory = GameHistory(id, word, difficulty, duration, date)       //Chacune des valeurs stockés les ajoute dans UN GameHistory qui va ensuite etre ajouté dans notre liste
                historyList.add(gameHistory)
            } while (cursor.moveToNext())          //Tant que qu'il y a des lignes suivantes
        }
        cursor.close()
        db.close()
        return historyList   //Retournes notre liste contenant toutes l'historique des parties jouées
    }

    // Insère un mot dans le dictionnaire et ensuite retourne la table en question avec les valeurs
    fun insertWord(word: String, difficulty: String, language: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_WORD_TEXT, word)
        values.put(COLUMN_WORD_DIFFICULTY, difficulty)
        values.put(COLUMN_WORD_LANGUAGE, language)

        val success = db.insert(TABLE_DICTIONARY, null, values)
        db.close()
        return success
    }

    // Supprime un mot du dictionnaire
    fun deleteWord(word: String): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_DICTIONARY, "$COLUMN_WORD_TEXT = ?", arrayOf(word))
        db.close()
        return success > 0  //Vu que la méthode delete retourne le nbr de lignes affectées et qu'on le store dans success, si il a actualy delete une ligne ca va etre true vu que 1 > 0
    }

    // Récupère tous les mots du dictionnaire en fonction de la langue et de la difficulté et les retourne dans la liste wordList
    fun getAllWords(language: String, difficulty: String): List<String> {
        val wordList = ArrayList<String>()
        val selectQuery = "SELECT * FROM $TABLE_DICTIONARY WHERE $COLUMN_WORD_LANGUAGE = ? AND $COLUMN_WORD_DIFFICULTY = ?"
        val db = this.readableDatabase
        var cursor: Cursor? = null


            cursor = db.rawQuery(selectQuery, arrayOf(language, difficulty))


        if (cursor.moveToFirst()) {
            do {
                val word = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORD_TEXT))
                wordList.add(word)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return wordList
    }

    // Sauvegarde une préférence séléctionné
    fun savePreference(key: String, value: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_KEY, key)
        values.put(COLUMN_VALUE, value)
        db.insertWithOnConflict(TABLE_PREFERENCES, null, values, SQLiteDatabase.CONFLICT_REPLACE) //remplace lancienne cle primaire par la nouvelle
        db.close()
    }


    fun getPreference(key: String, defaultValue: String): String { // Récupère une préférence
        val db = this.readableDatabase
        val cursor = db.query(TABLE_PREFERENCES, arrayOf(COLUMN_VALUE), "$COLUMN_KEY=?", arrayOf(key), null, null, null) //https://stackoverflow.com/questions/49432593/im-getting-error-whenever-i-try-to-get-data-from-my-db-using-this-functionim
        return if (cursor.moveToFirst()) {
            val value = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE))
            cursor.close()
            db.close()
            value
        } else {
            cursor.close()
            db.close()
            defaultValue
        }
    }
}
