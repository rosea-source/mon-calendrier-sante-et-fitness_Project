package com.example.mon_calendrier_sante_et_fitness.contract

interface AccueilContract {

    data class JourCalendrier(
        val numero: Int,
        val estDansMois: Boolean,
        val estAujourdhui: Boolean,
        val aSeance: Boolean
    )

    interface View {
        fun afficherSeancesParJour(seancesParJour: Map<Long, Boolean>)
        fun afficherNombreSeances(nombre: Int)
        fun afficherTotalHeures(heures: String)
        fun afficherJours(jours: List<JourCalendrier>)
    }

    interface Presentateur {
        fun chargerSeancesParJour()
        fun chargerStats()
        fun calculerJoursDuMois(annee: Int, mois: Int, seancesParJour: Map<Long, Boolean>)
    }
}