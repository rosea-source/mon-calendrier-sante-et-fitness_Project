package com.example.mon_calendrier_sante_et_fitness.presentateur

import com.example.mon_calendrier_sante_et_fitness.contract.AjouterSeanceContract
import com.example.mon_calendrier_sante_et_fitness.data.local.entity.Seance
import com.example.mon_calendrier_sante_et_fitness.data.repository.SeanceRepository

class SeancePresentateur(
    private var vue: AjouterSeanceContract.View?,
    private val repository: SeanceRepository
) : AjouterSeanceContract.Presentateur {

    override fun enregistrerExercice(nom: String, duree: String, poids: String, categorieId: Long, date: Long) {

        if (nom.isBlank()) {
            vue?.afficherErreur("Le nom de l'exercice est obligatoire.")
            return
        }
        val dureeInt = duree.toIntOrNull()
        if (dureeInt == null || dureeInt <= 0) {
            vue?.afficherErreur("La durée doit être un nombre entier positif.")
            return
        }
        val poidsDouble = poids.toDoubleOrNull()
        if (poidsDouble == null || poidsDouble <= 0.0) {
            vue?.afficherErreur("Le poids doit être un nombre positif.")
            return
        }
        if (categorieId <= 0L) {
            vue?.afficherErreur("Veuillez sélectionner une catégorie.")
            return
        }

        try {
            repository.ajouterSeance(
                typeId = categorieId,
                duree = dureeInt,
                intensite = nom,
                poids = poidsDouble,
                notes = null,
                date = date
            )
            vue?.afficherSucces()
            vue?.fermerEcran()
        } catch (e: Exception) {
            vue?.afficherErreur("Erreur lors de l'enregistrement. Réessayez.")
        }
    }

    override fun modifierExercice(id: Long, nom: String, duree: String, poids: String, categorieId: Long, date: Long) {
        // Validation
        if (nom.isBlank()) {
            vue?.afficherErreur("Le nom de l'exercice est obligatoire.")
            return
        }
        val dureeInt = duree.toIntOrNull()
        if (dureeInt == null || dureeInt <= 0) {
            vue?.afficherErreur("La durée doit être un nombre entier positif.")
            return
        }
        val poidsDouble = poids.toDoubleOrNull()
        if (poidsDouble == null || poidsDouble <= 0.0) {
            vue?.afficherErreur("Le poids doit être un nombre positif.")
            return
        }

        try {
            repository.modifierSeance(
                id = id,
                typeId = categorieId,
                duree = dureeInt,
                intensite = nom,
                poids = poidsDouble,
                date = date
            )
            vue?.afficherSucces()
            vue?.fermerEcran()
        } catch (e: Exception) {
            vue?.afficherErreur("Erreur lors de la modification. Réessayez.")
        }
    }

    override fun supprimerExercice(id: Long) {
        try {
            repository.supprimerSeance(id)
            vue?.afficherSucces()
            vue?.fermerEcran()
        } catch (e: Exception) {
            vue?.afficherErreur("Erreur lors de la suppression. Réessayez.")
        }
    }

    fun onDestroy() {
        vue = null
    }
}