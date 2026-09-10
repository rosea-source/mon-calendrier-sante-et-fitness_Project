package com.example.mon_calendrier_sante_et_fitness.presentateur

import com.example.mon_calendrier_sante_et_fitness.contract.HistoriqueContract
import com.example.mon_calendrier_sante_et_fitness.data.repository.SeanceRepository
import java.util.Locale

class HistoriquePresentateur(
    private var vue: HistoriqueContract.View?,
    private val repository: SeanceRepository
) : HistoriqueContract.Presenter {

    override fun chargerHistorique() {
        try {
            val seances = repository.getAllSeances()
            val seancesTriees = seances.sortedBy { it.date }

            val totalMinutes = seances.sumOf { it.duree }
            val heures = totalMinutes / 60
            val minutes = totalMinutes % 60

            val poidsDebut = seancesTriees.firstOrNull()?.poids ?: 0.0
            val poidsActuel = seancesTriees.lastOrNull()?.poids ?: 0.0
            val poidsPerdu = if (seancesTriees.size >= 2) poidsDebut - poidsActuel else 0.0

            vue?.afficherTotalHeures("${heures}h ${minutes}min")
            vue?.afficherNombreSeances(seances.size)
            vue?.afficherPoidsPerdu(
                String.format(Locale.CANADA_FRENCH, "%.1f kg", poidsPerdu)
            )

        } catch (e: Exception) {
            vue?.afficherErreur("Erreur lors du chargement de l'historique")
        }
    }

    override fun onDestroy() {
        vue = null
    }
}