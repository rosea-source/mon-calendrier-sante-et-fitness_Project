package com.example.mon_calendrier_sante_et_fitness

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matchers.startsWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AccueilEspressoTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    // ── Test bout-en-bout 1 ─────────────────────────────────────────────────
    // Vérifie que l'écran d'accueil s'affiche correctement avec le calendrier
    @Test
    fun accueil_affiche_calendrier_et_boutons() {
        onView(withId(R.id.tvTitre))
            .check(matches(isDisplayed()))

        onView(withId(R.id.gridCalendrier))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btnAjouter))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btnHistorique))
            .check(matches(isDisplayed()))
    }

    // ── Test bout-en-bout 2 ─────────────────────────────────────────────────
    // Vérifie que le total affiché est visible et a le bon format
    @Test
    fun accueil_total_minutes_affiche_format_correct() {
        onView(withId(R.id.tvTotalMinutes))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvTotalMinutes))
            .check(matches(withText(startsWith("Total cette semaine :"))))
    }

    // ── Test bout-en-bout 3 ─────────────────────────────────────────────────
    // Vérifie que cliquer sur Ajouter navigue vers le bon écran
    @Test
    fun clic_bouton_ajouter_ouvre_ecran_ajout() {
        onView(withId(R.id.btnAjouter))
            .perform(click())

        onView(withId(R.id.btnSauvegarder))
            .check(matches(isDisplayed()))
    }
}