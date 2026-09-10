package com.example.mon_calendrier_sante_et_fitness.contract

interface HistoriqueContract {

    interface View {
        fun afficherTotalHeures(total: String)
        fun afficherNombreSeances(nombre: Int)
        fun afficherPoidsPerdu(poidsPerdu: String)
        fun afficherErreur(message: String)
    }

    interface Presenter {
        fun chargerHistorique()
        fun onDestroy()
    }
}