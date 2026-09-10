package com.example.mon_calendrier_sante_et_fitness
// Code généré avec l'assistance de Claude (Anthropic) et adapté selon les besoins du projet.

import com.example.mon_calendrier_sante_et_fitness.contract.AccueilContract
import com.example.mon_calendrier_sante_et_fitness.data.local.entity.Seance
import com.example.mon_calendrier_sante_et_fitness.data.repository.SeanceRepository
import com.example.mon_calendrier_sante_et_fitness.presentateur.AccueilPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Calendar

class AccueilPresenterTest {

    private lateinit var vue: AccueilContract.View
    private lateinit var repository: SeanceRepository
    private lateinit var presenter: AccueilPresenter

    @Before
    fun setUp() {
        vue        = mock()
        repository = mock()
        presenter  = AccueilPresenter(vue, repository)
    }

    // ── Test 1 ──────────────────────────────────────────────────────────────
    // Interaction Présentateur : vérifie que afficherSeancesParJour() est appelé
    // sur la vue avec un Map vide quand il n'y a aucune séance
    @Test
    fun `chargerSeancesParJour appelle afficherSeancesParJour avec map vide si aucune seance`() {
        whenever(repository.getAllSeances()).thenReturn(emptyList())

        presenter.chargerSeancesParJour()

        verify(vue).afficherSeancesParJour(emptyMap())
    }

    // ── Test 2 ──────────────────────────────────────────────────────────────
    // Calcul Agrégé : vérifie que getTotalMinutes retourne le bon total
    // et que afficherTotalHeures reçoit le bon format "Xh Ymin"
    @Test
    fun `chargerStats affiche le bon total de minutes converti en heures`() {
        whenever(repository.getNombreSeances()).thenReturn(3)
        whenever(repository.getTotalMinutes()).thenReturn(90)

        presenter.chargerStats()

        verify(vue).afficherTotalHeures("1h 30min")
        verify(vue).afficherNombreSeances(3)
    }

    // ── Test 3 ──────────────────────────────────────────────────────────────
    // Calcul de jours : vérifie que calculerJoursDuMois retourne bien 42 jours

    @Test
    fun `calculerJoursDuMois retourne toujours 42 cellules`() {
        val seancesParJour = emptyMap<Long, Boolean>()
        var joursRecus: List<AccueilContract.JourCalendrier> = emptyList()

        whenever(vue.afficherJours(org.mockito.kotlin.any())).then {
            joursRecus = it.getArgument(0)
        }

        presenter.calculerJoursDuMois(2025, Calendar.MAY, seancesParJour)

        assert(joursRecus.size == 42)
    }

    // ── Test 4 ──────────────────────────────────────────────────────────────
    // Vérifie que les jours avec séance sont bien marqués aSeance = true
    @Test
    fun `calculerJoursDuMois marque correctement les jours avec seance`() {
        // Construire un timestamp pour le 1er mai 2025 à minuit
        val cal = Calendar.getInstance().apply {
            set(2025, Calendar.MAY, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timestampPremierMai = cal.timeInMillis
        val seancesParJour = mapOf(timestampPremierMai to true)

        var joursRecus: List<AccueilContract.JourCalendrier> = emptyList()
        whenever(vue.afficherJours(org.mockito.kotlin.any())).then {
            joursRecus = it.getArgument(0)
        }

        presenter.calculerJoursDuMois(2025, Calendar.MAY, seancesParJour)

        val jourAvecSeance = joursRecus.find { it.numero == 1 && it.estDansMois }
        assert(jourAvecSeance?.aSeance == true)
    }

    // ── Test 5 ──────────────────────────────────────────────────────────────
    // Vérifie que onDestroy() empêche tout appel à la vue (évite les fuites mémoire)
    @Test
    fun `apres onDestroy aucun appel a la vue`() {
        whenever(repository.getAllSeances()).thenReturn(emptyList())
        whenever(repository.getNombreSeances()).thenReturn(0)
        whenever(repository.getTotalMinutes()).thenReturn(0)

        presenter.onDestroy()
        presenter.chargerSeancesParJour()
        presenter.chargerStats()

        org.mockito.kotlin.verifyNoInteractions(vue)
    }
}