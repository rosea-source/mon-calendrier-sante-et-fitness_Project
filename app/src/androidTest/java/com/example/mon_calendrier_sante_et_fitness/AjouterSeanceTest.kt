package com.example.mon_calendrier_sante_et_fitness

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AjouterSeanceTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    //Test bout-en-bout (Utiliser le Medium Phone avec un API35 pour ce test-ci)
    @Test
    fun ajouterSeanceYoga() {

        // 1. Cliquer sur le bouton Ajouter
        onView(withId(R.id.btnAjouter)).perform(click())

        // 2. Remplir le nom
        onView(withId(R.id.editNom))
            .perform(typeText("Yoga"), closeSoftKeyboard())

        // 3. Remplir la durée
        onView(withId(R.id.editDuree))
            .perform(typeText("30"), closeSoftKeyboard())

        // 4. Remplir le poids
        onView(withId(R.id.editPoids))
            .perform(typeText("70.0"), closeSoftKeyboard())

        // 5. Cliquer sur Sauvegarder
        onView(withId(R.id.btnSauvegarder)).perform(click())

        // 6. Vérifier le retour à l'accueil
        onView(withId(R.id.btnAjouter)).check(matches(isDisplayed()))
    }
}