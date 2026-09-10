package com.example.mon_calendrier_sante_et_fitness.contract

interface AjouterSeanceContract {

    interface View
    {
        fun afficherSucces()
        fun afficherErreur(msg: String)
        fun fermerEcran()

    }

    interface Presentateur
    {
        fun enregistrerExercice(nom: String, duree: String, poids: String, categorieId: Long, date: Long)
        fun modifierExercice(id: Long, nom: String, duree: String, poids: String, categorieId: Long, date: Long)
        fun supprimerExercice(id: Long)

    }
}