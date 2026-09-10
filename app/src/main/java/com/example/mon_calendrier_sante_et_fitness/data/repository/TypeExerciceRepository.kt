package com.example.mon_calendrier_sante_et_fitness.data.repository

import android.content.ContentValues
import android.content.Context
import com.example.mon_calendrier_sante_et_fitness.data.local.database.FitnessDbHelper
import com.example.mon_calendrier_sante_et_fitness.data.local.entity.TypeExercice

class TypeExerciceRepository(context: Context) {
    private val helper = FitnessDbHelper(context)

    fun insert(type: TypeExercice): Long {
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            put("nom", type.nom)
        }
        return db.insert("TypeExercice", null, values)
    }

    fun getAll(): List<TypeExercice> {
        val db = helper.readableDatabase
        val list = mutableListOf<TypeExercice>()
        val cursor = db.query("TypeExercice", null, null, null, null, null, null)
        cursor.use {
            while (it.moveToNext()) {
                list.add(TypeExercice(
                    id = it.getLong(it.getColumnIndexOrThrow("id")),
                    nom = it.getString(it.getColumnIndexOrThrow("nom"))
                ))
            }
        }
        return list
    }
}