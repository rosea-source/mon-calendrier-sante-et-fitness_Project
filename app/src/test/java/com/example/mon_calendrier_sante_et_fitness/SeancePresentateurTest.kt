package com.example.mon_calendrier_sante_et_fitness

import com.example.mon_calendrier_sante_et_fitness.contract.AjouterSeanceContract
import com.example.mon_calendrier_sante_et_fitness.data.repository.SeanceRepository
import com.example.mon_calendrier_sante_et_fitness.presentateur.SeancePresentateur
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

class SeancePresentateurTest {

    private lateinit var vue: AjouterSeanceContract.View
    private lateinit var repository: SeanceRepository
    private lateinit var presentateur: SeancePresentateur

    @Before
    fun setup() {
        vue = mock()
        repository = mock()
        presentateur = SeancePresentateur(vue, repository)
    }

    //Test 1 : Validation d'insertion
    @Test
    fun enregistrerExerciceDureeZero() {
        presentateur.enregistrerExercice(
            nom         = "Yoga",
            duree       = "0",
            poids       = "70.0",
            categorieId = 1L,
            date = System.currentTimeMillis()
        )
        verify(vue).afficherErreur(any())
        verify(vue, never()).afficherSucces()
    }

    //Test 2: Option "Autre", donc un exercice avec un nom personnalisé
    @Test
    fun enregistrerExerciceNomPersonnalise() {
        presentateur.enregistrerExercice(
            nom         = "Leg press",
            duree       = "30",
            poids       = "70.0",
            categorieId = 1L,
            date = System.currentTimeMillis()
        )
        verify(vue, never()).afficherErreur(any())
    }

    // Test 3: Interaction Présentateur avec un nombre négatif
    @Test
    fun enregistrerExercicePoids() {
        presentateur.enregistrerExercice(
            nom         = "Course",
            duree       = "30",
            poids       = "-5.0",
            categorieId = 1L,
            date = System.currentTimeMillis()
        )
        verify(vue).afficherErreur(any())
        verify(vue, never()).afficherSucces()
    }
}