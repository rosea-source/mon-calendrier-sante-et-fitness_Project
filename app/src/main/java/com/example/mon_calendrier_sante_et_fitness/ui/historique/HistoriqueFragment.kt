package com.example.mon_calendrier_sante_et_fitness.ui.historique

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mon_calendrier_sante_et_fitness.R
import com.example.mon_calendrier_sante_et_fitness.data.local.PreferencesManager
import com.example.mon_calendrier_sante_et_fitness.data.local.entity.Seance
import com.example.mon_calendrier_sante_et_fitness.data.repository.SeanceRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HistoriqueFragment : Fragment(R.layout.fragment_historique) {

    private lateinit var seanceRepo: SeanceRepository
    private lateinit var prefsManager: PreferencesManager
    private lateinit var layoutSeances: LinearLayout
    private lateinit var seances: MutableList<Seance>

    private lateinit var editDateDebut: EditText
    private lateinit var editDateFin: EditText
    private lateinit var btnFiltrer: Button
    private lateinit var btnReinitialiser: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seanceRepo = SeanceRepository(requireContext())
        prefsManager = PreferencesManager(requireContext())
        layoutSeances = view.findViewById(R.id.layoutSeances)

        editDateDebut = view.findViewById(R.id.editDateDebut)
        editDateFin = view.findViewById(R.id.editDateFin)
        btnFiltrer = view.findViewById(R.id.btnFiltrer)
        btnReinitialiser = view.findViewById(R.id.btnReinitialiser)

        configurerCalendrier(editDateDebut)
        configurerCalendrier(editDateFin)

        view.findViewById<LinearLayout>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        btnFiltrer.setOnClickListener {
            filtrerParDate()
        }

        btnReinitialiser.setOnClickListener {
            editDateDebut.text.clear()
            editDateFin.text.clear()
            afficherListeSeances(seances)
        }

        chargerHistorique(view)
    }

    private fun configurerCalendrier(editText: EditText) {
        editText.setOnClickListener {
            val calendrier = Calendar.getInstance()

            DatePickerDialog(
                requireContext(),
                { _, annee, mois, jour ->
                    val date = String.format(
                        Locale.CANADA_FRENCH,
                        "%02d/%02d/%04d",
                        jour,
                        mois + 1,
                        annee
                    )

                    editText.setText(date)
                },
                calendrier.get(Calendar.YEAR),
                calendrier.get(Calendar.MONTH),
                calendrier.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun chargerHistorique(view: View) {

        seances = seanceRepo.getAllSeances().toMutableList()
        val seancesTriees = seances.sortedBy { it.date }

        val totalMinutes = seances.sumOf { it.duree }
        val heures = totalMinutes / 60
        val minutes = totalMinutes % 60

        val poidsDebut = seancesTriees.firstOrNull()?.poids ?: 0.0
        val poidsActuel = seancesTriees.lastOrNull()?.poids ?: 0.0
        val poidsObjectif = prefsManager.poidsObjectif.toDouble()

        val poidsPerdu =
            if (seancesTriees.size >= 2)
                poidsDebut - poidsActuel
            else
                0.0

        val poidsRestant =
            if (poidsObjectif > 0)
                poidsActuel - poidsObjectif
            else
                0.0

        view.findViewById<TextView>(R.id.tvTotalHeuresValeur).text =
            "${heures}h ${minutes}min"

        view.findViewById<TextView>(R.id.tvSeancesValeur).text =
            seances.size.toString()

        view.findViewById<TextView>(R.id.tvPoidsValeur).text =
            String.format(Locale.CANADA_FRENCH, "%.1f kg", poidsPerdu)

        view.findViewById<TextView>(R.id.tvPoidsRestantValeur).text =
            String.format(Locale.CANADA_FRENCH, "%.1f kg", poidsRestant)

        val graphiquePoids =
            view.findViewById<GraphiqueView>(R.id.graphiquePoids)

        graphiquePoids.type = GraphiqueView.Type.LIGNE
        graphiquePoids.valeurs =
            seancesTriees.map { it.poids.toFloat() }

        val graphiqueHeures =
            view.findViewById<GraphiqueView>(R.id.graphiqueHeures)

        graphiqueHeures.type = GraphiqueView.Type.BARRES
        graphiqueHeures.valeurs =
            seancesTriees.map { it.duree.toFloat() }

        afficherListeSeances(seances)
    }

    private fun filtrerParDate() {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.CANADA_FRENCH)

        val texteDebut = editDateDebut.text.toString().trim()
        val texteFin = editDateFin.text.toString().trim()

        if (texteDebut.isBlank() || texteFin.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Choisis une date début et une date fin",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        try {
            val dateDebut = format.parse(texteDebut)?.time ?: return
            val dateFinDebutJournee = format.parse(texteFin)?.time ?: return

            val calendrierFin = Calendar.getInstance()
            calendrierFin.timeInMillis = dateFinDebutJournee
            calendrierFin.set(Calendar.HOUR_OF_DAY, 23)
            calendrierFin.set(Calendar.MINUTE, 59)
            calendrierFin.set(Calendar.SECOND, 59)

            val dateFin = calendrierFin.timeInMillis

            val seancesFiltrees = seances.filter {
                it.date in dateDebut..dateFin
            }

            afficherListeSeances(seancesFiltrees)

        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Format demandé : jj/mm/aaaa",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun afficherListeSeances(liste: List<Seance>) {

        layoutSeances.removeAllViews()

        if (liste.isEmpty()) {

            val vide = TextView(requireContext()).apply {
                text = "Aucune séance trouvée"
                textSize = 14f
                setTextColor(0xFF777777.toInt())
                setPadding(16, 16, 16, 16)
            }

            layoutSeances.addView(vide)
            return
        }

        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH)

        liste.sortedByDescending { it.date }
            .forEach { seance ->

                val row = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                    setPadding(18, 14, 8, 14)
                    setBackgroundColor(0xFFFFFFFF.toInt())
                }

                val icon = TextView(requireContext()).apply {
                    text = "🏃"
                    textSize = 24f
                }

                val info = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.VERTICAL

                    layoutParams =
                        LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        )

                    setPadding(14, 0, 0, 0)
                }

                val date = TextView(requireContext()).apply {
                    text = sdf.format(Date(seance.date))
                    textSize = 12f
                    setTextColor(0xFF777777.toInt())
                }

                val titre = TextView(requireContext()).apply {
                    text = seance.intensite
                    textSize = 15f
                    setTypeface(null, android.graphics.Typeface.BOLD)
                    setTextColor(0xFF2C2C2A.toInt())
                }

                val details = TextView(requireContext()).apply {
                    text =
                        "${seance.duree} min  •  ${seance.poids} kg"

                    textSize = 13f
                    setTextColor(0xFF3B6D11.toInt())
                }

                val menu = TextView(requireContext()).apply {
                    text = "⋮"
                    textSize = 34f
                    setTextColor(0xFF000000.toInt())
                    gravity = Gravity.CENTER
                    setPadding(24, 8, 24, 8)
                }

                menu.setOnClickListener {
                    afficherMenu(menu, seance)
                }

                info.addView(date)
                info.addView(titre)
                info.addView(details)

                row.addView(icon)
                row.addView(info)
                row.addView(menu)

                val params =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = 10
                    }

                layoutSeances.addView(row, params)
            }
    }

    private fun afficherMenu(anchor: View, seance: Seance) {

        // Aide avec IA (ChatGPT)
        // Pour m'aider à créer un popup menu pour modifier ou supprimer une séance de l'historique.
        val popup = PopupMenu(requireContext(), anchor)

        popup.menu.add("Modifier")
        popup.menu.add("Supprimer")

        popup.setOnMenuItemClickListener {

            when (it.title.toString()) {

                "Modifier" -> {

                    val bundle = Bundle().apply {
                        putLong("seanceId", seance.id)
                    }

                    findNavController().navigate(
                        R.id.action_historique_to_ajouter,
                        bundle
                    )

                    true
                }

                "Supprimer" -> {

                    AlertDialog.Builder(requireContext())
                        .setTitle("Supprimer la séance?")
                        .setMessage(
                            "Cette action va enlever cette séance de ton historique."
                        )
                        .setPositiveButton("Supprimer") { _, _ ->

                            seanceRepo.supprimerSeance(seance.id)

                            chargerHistorique(requireView())
                        }
                        .setNegativeButton("Annuler", null)
                        .show()

                    true
                }

                else -> false
            }
        }

        popup.show()
    }
}