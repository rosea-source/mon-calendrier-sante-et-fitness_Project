package com.example.mon_calendrier_sante_et_fitness

import com.example.mon_calendrier_sante_et_fitness.contract.HistoriqueContract
import com.example.mon_calendrier_sante_et_fitness.data.local.entity.Seance
import com.example.mon_calendrier_sante_et_fitness.data.repository.SeanceRepository
import com.example.mon_calendrier_sante_et_fitness.presentateur.HistoriquePresentateur
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class HistoriquePresentateurTest {

    private lateinit var vue: HistoriqueContract.View
    private lateinit var repository: SeanceRepository
    private lateinit var presentateur: HistoriquePresentateur

    // Aide avec IA (ChatGPT)
    // Pour m'aider sur les tests unitaires avec Mockito et non simple.
    @Before
    fun setup() {
        vue = mock()
        repository = mock()
        presentateur = HistoriquePresentateur(vue, repository)
    }

    // Test 1 : Calcul agrégé
    // Vérifie que le présentateur calcule correctement le total des heures.
    @Test
    fun chargerHistoriqueCalculeTotalHeures() {
        val seance1: Seance = mock()
        val seance2: Seance = mock()

        whenever(seance1.duree).thenReturn(30)
        whenever(seance2.duree).thenReturn(60)
        whenever(seance1.date).thenReturn(1000L)
        whenever(seance2.date).thenReturn(2000L)
        whenever(seance1.poids).thenReturn(75.0)
        whenever(seance2.poids).thenReturn(74.0)

        whenever(repository.getAllSeances()).thenReturn(listOf(seance1, seance2))

        presentateur.chargerHistorique()

        verify(vue).afficherTotalHeures("1h 30min")
    }

    // Test 2 : Nombre de séances
    // Vérifie que le présentateur affiche le bon nombre de séances réalisées.
    @Test
    fun chargerHistoriqueAfficheNombreSeances() {
        val seance1: Seance = mock()
        val seance2: Seance = mock()
        val seance3: Seance = mock()

        whenever(seance1.date).thenReturn(1000L)
        whenever(seance2.date).thenReturn(2000L)
        whenever(seance3.date).thenReturn(3000L)

        whenever(seance1.duree).thenReturn(10)
        whenever(seance2.duree).thenReturn(20)
        whenever(seance3.duree).thenReturn(30)

        whenever(seance1.poids).thenReturn(75.0)
        whenever(seance2.poids).thenReturn(74.5)
        whenever(seance3.poids).thenReturn(74.0)

        whenever(repository.getAllSeances()).thenReturn(listOf(seance1, seance2, seance3))

        presentateur.chargerHistorique()

        verify(vue).afficherNombreSeances(3)
    }

    // Test 3 : Calcul du poids perdu
    // Vérifie que le présentateur calcule la différence entre le premier poids et le dernier poids.
    @Test
    fun chargerHistoriqueCalculePoidsPerdu() {
        val seanceDebut: Seance = mock()
        val seanceFin: Seance = mock()

        whenever(seanceDebut.date).thenReturn(1000L)
        whenever(seanceFin.date).thenReturn(2000L)

        whenever(seanceDebut.duree).thenReturn(30)
        whenever(seanceFin.duree).thenReturn(30)

        whenever(seanceDebut.poids).thenReturn(75.5)
        whenever(seanceFin.poids).thenReturn(72.0)

        whenever(repository.getAllSeances()).thenReturn(listOf(seanceDebut, seanceFin))

        presentateur.chargerHistorique()

        verify(vue).afficherPoidsPerdu("3,5 kg")
    }

    // Test 4 : Erreur du repository
    // Vérifie que la vue reçoit un message d'erreur si le repository plante.
    @Test
    fun chargerHistoriqueRepositoryErreur() {
        whenever(repository.getAllSeances()).thenThrow(RuntimeException("Erreur BD"))

        presentateur.chargerHistorique()

        verify(vue).afficherErreur(any())
    }
}