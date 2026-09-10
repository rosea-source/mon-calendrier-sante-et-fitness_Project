package com.example.mon_calendrier_sante_et_fitness.data.local

import android.content.Context

class PreferencesManager(context: Context) {

    private val prefs = context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val CLE_PRENOM = "prenom"
        private const val CLE_POIDS_OBJECTIF = "poids_objectif"
        private const val CLE_UNITE_POIDS = "unite_poids"
    }

    var prenom: String
        get() = prefs.getString(CLE_PRENOM, "") ?: ""
        set(value) = prefs.edit().putString(CLE_PRENOM, value).apply()

    var poidsObjectif: Float
        get() = prefs.getFloat(CLE_POIDS_OBJECTIF, 0f)
        set(value) = prefs.edit().putFloat(CLE_POIDS_OBJECTIF, value).apply()

    var unitesPoids: String
        get() = prefs.getString(CLE_UNITE_POIDS, "kg") ?: "kg"
        set(value) = prefs.edit().putString(CLE_UNITE_POIDS, value).apply()
}