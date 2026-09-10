package com.example.mon_calendrier_sante_et_fitness.data.repository

import android.content.ContentValues
import android.content.Context
import com.example.mon_calendrier_sante_et_fitness.data.local.database.FitnessDbHelper
import com.example.mon_calendrier_sante_et_fitness.data.local.entity.Seance

class SeanceRepository(context: Context) {
    private val helper = FitnessDbHelper(context)



    fun ajouterSeance(
        typeId: Long,
        duree: Int,
        intensite: String,
        poids: Double,
        notes: String?,
        date: Long = System.currentTimeMillis()
    ): Long {
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            put("typeId", typeId)
            put("date", date)
            put("date", System.currentTimeMillis())
            put("duree", duree)
            put("intensite", intensite)
            put("poids", poids)
            put("notes", notes)
        }
        return db.insert("Seance", null, values)
    }

    fun modifierSeance(
        id: Long,
        typeId: Long,
        duree: Int,
        intensite: String,
        poids: Double,
        date: Long = System.currentTimeMillis()
    ): Int {
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            put("typeId", typeId)
            put("date", date)
            put("duree", duree)
            put("intensite", intensite)
            put("poids", poids)
        }
        return db.update("Seance", values, "id = ?", arrayOf(id.toString()))
    }

    fun supprimerSeance(id: Long): Int {
        val db = helper.writableDatabase
        return db.delete("Seance", "id = ?", arrayOf(id.toString()))
    }

    // ── Appelé par AccueilPresenter ──

    fun getAllSeances(): List<Seance> {
        val db = helper.readableDatabase
        val list = mutableListOf<Seance>()
        val cursor = db.query("Seance", null, null, null, null, null, "date DESC")
        cursor.use {
            while (it.moveToNext()) list.add(cursorToSeance(it))
        }
        return list
    }

    fun getNombreSeances(): Int {
        val db = helper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM Seance", null)
        return cursor.use { if (it.moveToFirst()) it.getInt(0) else 0 }
    }

    fun getTotalMinutes(): Int {
        val db = helper.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(duree) FROM Seance", null)
        return cursor.use { if (it.moveToFirst()) it.getInt(0) else 0 }
    }

    // ── Autres méthodes utiles ──

    fun getById(id: Long): Seance? {
        val db = helper.readableDatabase
        val cursor = db.query("Seance", null, "id = ?", arrayOf(id.toString()), null, null, null)
        return cursor.use { if (it.moveToFirst()) cursorToSeance(it) else null }
    }

    fun getByType(typeId: Long): List<Seance> {
        val db = helper.readableDatabase
        val list = mutableListOf<Seance>()
        val cursor = db.query("Seance", null, "typeId = ?", arrayOf(typeId.toString()), null, null, null)
        cursor.use { while (it.moveToNext()) list.add(cursorToSeance(it)) }
        return list
    }

    // ── Curseur → objet ──

    private fun cursorToSeance(cursor: android.database.Cursor): Seance {
        return Seance(
            id        = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
            typeId    = cursor.getLong(cursor.getColumnIndexOrThrow("typeId")),
            date      = cursor.getLong(cursor.getColumnIndexOrThrow("date")),
            duree     = cursor.getInt(cursor.getColumnIndexOrThrow("duree")),
            intensite = cursor.getString(cursor.getColumnIndexOrThrow("intensite")),
            poids     = cursor.getDouble(cursor.getColumnIndexOrThrow("poids")),
            notes     = cursor.getString(cursor.getColumnIndexOrThrow("notes"))
        )
    }
}