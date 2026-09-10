package com.example.mon_calendrier_sante_et_fitness.data.local.database
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class FitnessDbHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "fitness_db.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
        CREATE TABLE Seance (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        typeId INTEGER NOT NULL,
        date INTEGER NOT NULL,
        duree INTEGER NOT NULL,
        intensite TEXT NOT NULL,
        poids REAL NOT NULL,
        notes TEXT,
        FOREIGN KEY(typeId) REFERENCES TypeExercice(id)
        )
    """.trimIndent())

        db.execSQL("""
        CREATE TABLE TypeExercice (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nom TEXT NOT NULL
        )
    """.trimIndent())

        db.execSQL("""
        CREATE TABLE Pesee (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            poids REAL NOT NULL,
            date INTEGER NOT NULL
        )
    """.trimIndent())

        //Insértion des types par defauts
        listOf("Cardio", "Musculation", "Yoga", "Course", "Natation", "Autre")
            .forEach { nom ->
                db.execSQL("INSERT INTO TypeExercice (nom) VALUES ('$nom')")
            }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}