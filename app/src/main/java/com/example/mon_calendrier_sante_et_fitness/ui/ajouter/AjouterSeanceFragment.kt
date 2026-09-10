package com.example.mon_calendrier_sante_et_fitness.ui.ajouter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import java.util.Calendar
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Locale
import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mon_calendrier_sante_et_fitness.R
import com.example.mon_calendrier_sante_et_fitness.contract.AjouterSeanceContract
import com.example.mon_calendrier_sante_et_fitness.data.local.entity.TypeExercice
import com.example.mon_calendrier_sante_et_fitness.data.repository.SeanceRepository
import com.example.mon_calendrier_sante_et_fitness.data.repository.TypeExerciceRepository
import com.example.mon_calendrier_sante_et_fitness.presentateur.SeancePresentateur
import java.sql.Date

class AjouterSeanceFragment : Fragment(), AjouterSeanceContract.View {

    private lateinit var seanceRepo: SeanceRepository
    private lateinit var presentateur: SeancePresentateur

    private lateinit var spinnerType: Spinner
    private lateinit var editNom: EditText
    private lateinit var editDuree: EditText
    private lateinit var editPoids: EditText
    private lateinit var btnSauvegarder: Button

    //Utilisation de IA(Gemini)
    private lateinit var tvDateSelectionnee: TextView
    private lateinit var layoutDate: LinearLayout
    private var dateSelectionnee: Long = System.currentTimeMillis()

    private var typesExercice: List<TypeExercice> = emptyList()

    // -1L = création
    private var seanceId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_ajouter_seance,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seanceId = arguments?.getLong("seanceId", -1L) ?: -1L

        initialiserPresentateur()
        initialiserVues(view)
        chargerTypesExercice()
        configurerBoutonSauvegarder()
        configurerBoutonDate()

        view.findViewById<LinearLayout>(R.id.btnRetourAccueil)
            .setOnClickListener {
                findNavController().popBackStack()
            }

        if (seanceId != -1L) {
            chargerSeancePourModification()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presentateur.onDestroy()
    }

    private fun initialiserPresentateur() {
        seanceRepo = SeanceRepository(requireContext())
        presentateur = SeancePresentateur(this, seanceRepo)
    }

    private fun initialiserVues(view: View) {
        spinnerType = view.findViewById(R.id.spinnerType)
        editNom = view.findViewById(R.id.editNom)
        editDuree = view.findViewById(R.id.editDuree)
        editPoids = view.findViewById(R.id.editPoids)
        btnSauvegarder = view.findViewById(R.id.btnSauvegarder)
        layoutDate = view.findViewById(R.id.layoutDate)
        tvDateSelectionnee = view.findViewById(R.id.tvDateSelectionnee)

    }

    private fun chargerTypesExercice() {
        val typeRepo = TypeExerciceRepository(requireContext())

        typesExercice = typeRepo.getAll()

        val noms = typesExercice.map { it.nom }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            noms
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerType.adapter = adapter
    }

    private fun configurerBoutonSauvegarder() {

        btnSauvegarder.setOnClickListener {

            val categorieId =
                if (typesExercice.isNotEmpty())
                    typesExercice[spinnerType.selectedItemPosition].id
                else
                    0L

            if (seanceId == -1L) {

                presentateur.enregistrerExercice(
                    nom = editNom.text.toString(),
                    duree = editDuree.text.toString(),
                    poids = editPoids.text.toString(),
                    categorieId = categorieId,
                    date = dateSelectionnee
                )

            } else {

                presentateur.modifierExercice(
                    id = seanceId,
                    nom = editNom.text.toString(),
                    duree = editDuree.text.toString(),
                    poids = editPoids.text.toString(),
                    categorieId = categorieId,
                    date = dateSelectionnee
                )
            }
        }
    }

    private fun chargerSeancePourModification() {

        val seance = seanceRepo.getById(seanceId) ?: return

        editNom.setText(seance.intensite)
        editDuree.setText(seance.duree.toString())
        editPoids.setText(seance.poids.toString())

        val index =
            typesExercice.indexOfFirst {
                it.id == seance.typeId
            }

        if (index >= 0) {
            spinnerType.setSelection(index)
        }

        btnSauvegarder.text = "Modifier"
    }


    //Generer par IA(Gemini)
    private fun configurerBoutonDate() {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.FRENCH)
        tvDateSelectionnee.text = sdf.format(Date(dateSelectionnee))

        layoutDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    cal.set(year, month, day, 0, 0, 0)
                    cal.set(Calendar.MILLISECOND, 0)
                    dateSelectionnee = cal.timeInMillis
                    tvDateSelectionnee.text = sdf.format(Date(dateSelectionnee))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun afficherSucces() {

        val message =
            if (seanceId == -1L)
                "Séance enregistrée !"
            else
                "Séance modifiée !"

        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun afficherErreur(msg: String) {
        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun fermerEcran() {
        findNavController().popBackStack()
    }
}