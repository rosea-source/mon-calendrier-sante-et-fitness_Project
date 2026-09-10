package com.example.mon_calendrier_sante_et_fitness.data.local.entity

data class Seance(
    val id: Long = 0,
    val typeId: Long,
    val duree: Int,
    val intensite: String,
    val poids: Double,
    val notes: String?,
    val date: Long
)