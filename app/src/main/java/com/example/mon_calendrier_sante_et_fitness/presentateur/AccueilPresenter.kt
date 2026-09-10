package com.example.mon_calendrier_sante_et_fitness.presentateur

import com.example.mon_calendrier_sante_et_fitness.contract.AccueilContract
import com.example.mon_calendrier_sante_et_fitness.data.repository.SeanceRepository
import java.util.Calendar

class AccueilPresenter(
    private var vue: AccueilContract.View?,
    private val repository: SeanceRepository
) : AccueilContract.Presentateur {

    override fun chargerSeancesParJour() {
        val seances = repository.getAllSeances()
        val map = seances.associate { seance ->
            debutDuJour(seance.date) to true
        }
        vue?.afficherSeancesParJour(map)
    }

    override fun chargerStats() {
        val nombre   = repository.getNombreSeances()
        val totalMin = repository.getTotalMinutes()
        val h   = totalMin / 60
        val min = totalMin % 60
        vue?.afficherNombreSeances(nombre)
        vue?.afficherTotalHeures("${h}h ${min}min")
    }

    override fun calculerJoursDuMois(
        annee: Int,
        mois: Int,
        seancesParJour: Map<Long, Boolean>
    ) {
        val cal = Calendar.getInstance()
        cal.set(annee, mois, 1)

        var premierJour = cal.get(Calendar.DAY_OF_WEEK) - 2
        if (premierJour < 0) premierJour = 6

        val nbJoursMois = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val aujourdhui     = Calendar.getInstance()
        val estMoisActuel  = mois == aujourdhui.get(Calendar.MONTH)
                && annee == aujourdhui.get(Calendar.YEAR)
        val jourAujourdhui = aujourdhui.get(Calendar.DAY_OF_MONTH)

        val jours = mutableListOf<AccueilContract.JourCalendrier>()

        for (i in 0 until 42) {
            val jourDuMois  = i - premierJour + 1
            val estDansMois = jourDuMois in 1..nbJoursMois

            val calJour = Calendar.getInstance().apply {
                set(annee, mois, if (estDansMois) jourDuMois else 1, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val timestamp     = calJour.timeInMillis
            val aSeance       = estDansMois && (seancesParJour[timestamp] ?: false)
            val estAujourdhui = estDansMois && estMoisActuel && jourDuMois == jourAujourdhui

            jours.add(
                AccueilContract.JourCalendrier(
                    numero        = if (estDansMois) jourDuMois else 0,
                    estDansMois   = estDansMois,
                    estAujourdhui = estAujourdhui,
                    aSeance       = aSeance
                )
            )
        }

        vue?.afficherJours(jours)
    }

    fun onDestroy() {
        vue = null
    }

    private fun debutDuJour(timestamp: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}