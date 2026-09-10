package com.example.mon_calendrier_sante_et_fitness.data.repository

import android.content.ContentValues
import android.content.Context
import com.example.mon_calendrier_sante_et_fitness.data.local.database.FitnessDbHelper
import com.example.mon_calendrier_sante_et_fitness.data.local.entity.Pesee

class PeseeRepository(context: Context) {
    private val helper = FitnessDbHelper(context)

    fun insert(pesee: Pesee): Long {
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            put("poids", pesee.poids)
            put("date", pesee.date)
        }
        return db.insert("Pesee", null, values)
    }

    fun getAll(): List<Pesee> {
        val db = helper.readableDatabase
        val list = mutableListOf<Pesee>()
        val cursor = db.query("Pesee", null, null, null, null, null, "date ASC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(Pesee(
                    id = it.getLong(it.getColumnIndexOrThrow("id")),
                    poids = it.getDouble(it.getColumnIndexOrThrow("poids")),
                    date = it.getLong(it.getColumnIndexOrThrow("date"))
                ))
            }
        }
        return list
    }

    fun getDernierePesee(): Pesee? {
        val db = helper.readableDatabase
        val cursor = db.query("Pesee", null, null, null, null, null, "date DESC", "1")
        return cursor.use {
            if (it.moveToFirst()) Pesee(
                id = it.getLong(it.getColumnIndexOrThrow("id")),
                poids = it.getDouble(it.getColumnIndexOrThrow("poids")),
                date = it.getLong(it.getColumnIndexOrThrow("date"))
            ) else null
        }
    }
}