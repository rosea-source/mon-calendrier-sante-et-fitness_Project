package com.example.mon_calendrier_sante_et_fitness

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoriqueEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // Test bout-en-bout : Consultation de l'historique
    // Vérifie que l'utilisateur peut ouvrir l'écran Historique depuis l'accueil et que les principales sections sont affichées.
    @Test
    fun ouvrirHistorique_depuisAccueil_affichePageHistorique() {

        // Cliquer sur le bouton Historique depuis l'accueil
        onView(withText("Historique"))
            .perform(click())

        // Vérifier que la section Aperçu est visible
        onView(withText("Aperçu"))
            .check(matches(isDisplayed()))

        // Vérifier que le graphique d'évolution du poids est affiché
        onView(withText("Évolution du poids"))
            .check(matches(isDisplayed()))

        // Vérifier que la liste des dernières séances est affichée
        onView(withText("Dernières séances"))
            .check(matches(isDisplayed()))
    }
}