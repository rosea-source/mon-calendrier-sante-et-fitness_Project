package com.example.mon_calendrier_sante_et_fitness.ui.accueil
// Code généré avec l'assistance de Claude (Anthropic) et adapté selon les besoins du projet.

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mon_calendrier_sante_et_fitness.R
import com.example.mon_calendrier_sante_et_fitness.contract.AccueilContract
import com.example.mon_calendrier_sante_et_fitness.data.local.PreferencesManager
import com.example.mon_calendrier_sante_et_fitness.data.repository.SeanceRepository
import com.example.mon_calendrier_sante_et_fitness.presentateur.AccueilPresenter
import java.text.SimpleDateFormat
import java.util.*

class AccueilFragment : Fragment(), AccueilContract.View {

    private lateinit var presenter: AccueilPresenter
    private lateinit var prefsManager: PreferencesManager
    private lateinit var gridCalendrier: GridLayout
    private lateinit var tvMoisAnnee: TextView
    private lateinit var tvBonjour: TextView
    private lateinit var tvTotalMinutes: TextView
    private lateinit var btnMoisPrecedent: ImageButton
    private lateinit var btnMoisSuivant: ImageButton
    private lateinit var btnAjouter: CardView
    private lateinit var btnHistorique: CardView
    private lateinit var ivAvatar: ImageView

    private val calendrierCourant = Calendar.getInstance()
    private var seancesParJourCourant: Map<Long, Boolean> = emptyMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_accueil, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = SeanceRepository(requireContext())
        prefsManager   = PreferencesManager(requireContext())
        presenter      = AccueilPresenter(this, repository)

        gridCalendrier   = view.findViewById(R.id.gridCalendrier)
        tvMoisAnnee      = view.findViewById(R.id.tvMoisAnnee)
        tvBonjour        = view.findViewById(R.id.tvBonjour)
        tvTotalMinutes   = view.findViewById(R.id.tvTotalMinutes)
        btnMoisPrecedent = view.findViewById(R.id.btnMoisPrecedent)
        btnMoisSuivant   = view.findViewById(R.id.btnMoisSuivant)
        btnAjouter       = view.findViewById(R.id.btnAjouter)
        btnHistorique    = view.findViewById(R.id.btnHistorique)
        ivAvatar         = view.findViewById(R.id.ivAvatar)

        mettreAJourBonjour()
        setupCalendrier()
        setupBoutons()
        setupAvatar()

        presenter.chargerSeancesParJour()
        presenter.chargerStats()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    // ─────────────────────────────────────────
    //  ACCUEILCONTRACT.VIEW
    // ─────────────────────────────────────────

    override fun afficherSeancesParJour(seancesParJour: Map<Long, Boolean>) {
        seancesParJourCourant = seancesParJour
        rafraichirCalendrier()
    }

    override fun afficherNombreSeances(nombre: Int) {
        // brancher sur un TextView si ajouté plus tard
    }

    override fun afficherTotalHeures(heures: String) {
        tvTotalMinutes.text = "Total cette semaine : $heures"
    }

    override fun afficherJours(jours: List<AccueilContract.JourCalendrier>) {
        gridCalendrier.removeAllViews()
        jours.forEach { jour ->
            val cellule = creerCelluleJour(
                numero        = jour.numero,
                estDansMois   = jour.estDansMois,
                estAujourdhui = jour.estAujourdhui,
                aSeance       = jour.aSeance
            )
            val params = GridLayout.LayoutParams().apply {
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec    = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                width      = 0
                height     = GridLayout.LayoutParams.WRAP_CONTENT
            }
            gridCalendrier.addView(cellule, params)
        }
    }

    // ─────────────────────────────────────────
    //  PRÉFÉRENCES
    // ─────────────────────────────────────────

    private fun mettreAJourBonjour() {
        val prenom = prefsManager.prenom
        tvBonjour.text = if (prenom.isNotBlank()) "Bonjour $prenom 👋" else "Bonjour 👋"
    }

    private fun setupAvatar() {
        ivAvatar.setOnClickListener { afficherDialogPreferences() }
    }

    private fun afficherDialogPreferences() {
        val dialog = Dialog(requireContext())
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_preferences, null)
        dialog.setContentView(dialogView)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val etPrenom        = dialogView.findViewById<EditText>(R.id.etPrenom)
        val etPoidsObjectif = dialogView.findViewById<EditText>(R.id.etPoidsObjectif)
        val btnSauvegarder  = dialogView.findViewById<Button>(R.id.btnSauvegarder)

        if (prefsManager.prenom.isNotBlank())
            etPrenom.setText(prefsManager.prenom)
        if (prefsManager.poidsObjectif > 0f)
            etPoidsObjectif.setText(prefsManager.poidsObjectif.toString())

        btnSauvegarder.setOnClickListener {
            val prenomTexte = etPrenom.text.toString().trim()
            if (prenomTexte.isNotBlank()) prefsManager.prenom = prenomTexte

            val poids = etPoidsObjectif.text.toString().toFloatOrNull()
            if (poids != null && poids > 0f) prefsManager.poidsObjectif = poids

            mettreAJourBonjour()
            dialog.dismiss()
            Toast.makeText(requireContext(), "Préférences sauvegardées ✓", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    // ─────────────────────────────────────────
    //  CALENDRIER
    // ─────────────────────────────────────────

    private fun setupCalendrier() {
        btnMoisPrecedent.setOnClickListener {
            calendrierCourant.add(Calendar.MONTH, -1)
            rafraichirCalendrier()
        }
        btnMoisSuivant.setOnClickListener {
            calendrierCourant.add(Calendar.MONTH, 1)
            rafraichirCalendrier()
        }
    }

    private fun rafraichirCalendrier() {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.FRENCH)
        tvMoisAnnee.text = sdf.format(calendrierCourant.time)
            .replaceFirstChar { it.uppercase() }

        presenter.calculerJoursDuMois(
            annee          = calendrierCourant.get(Calendar.YEAR),
            mois           = calendrierCourant.get(Calendar.MONTH),
            seancesParJour = seancesParJourCourant
        )
    }

    private fun creerCelluleJour(
        numero: Int,
        estDansMois: Boolean,
        estAujourdhui: Boolean,
        aSeance: Boolean
    ): LinearLayout {
        val ctx = requireContext()
        val cellule = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            gravity     = Gravity.CENTER
            setPadding(0, 6, 0, 6)
        }

        val tvNumero = TextView(ctx).apply {
            text     = if (estDansMois) numero.toString() else ""
            textSize = 12.5f
            gravity  = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(52, 52).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            when {
                estAujourdhui -> {
                    background = ContextCompat.getDrawable(ctx, R.drawable.bg_jour_aujourd_hui)
                    setTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    setTypeface(null, Typeface.BOLD)
                }
                estDansMois -> {
                    setTextColor(ContextCompat.getColor(ctx, R.color.textPrimary))
                }
                else -> {
                    setTextColor(ContextCompat.getColor(ctx, R.color.textTertiary))
                }
            }
        }
        cellule.addView(tvNumero)

        val dot = View(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(10, 10).apply {
                gravity   = Gravity.CENTER_HORIZONTAL
                topMargin = 3
            }
            if (estDansMois) {
                background = ContextCompat.getDrawable(
                    ctx,
                    if (aSeance) R.drawable.bg_dot_green else R.drawable.bg_dot_red
                )
            }
        }
        cellule.addView(dot)

        return cellule
    }

    // ─────────────────────────────────────────
    //  BOUTONS NAVIGATION
    // ─────────────────────────────────────────

    private fun setupBoutons() {
        btnAjouter.setOnClickListener {
            findNavController().navigate(R.id.action_accueil_to_ajouter)
        }
        btnHistorique.setOnClickListener {
            findNavController().navigate(R.id.action_accueil_to_historique)
        }
    }
}