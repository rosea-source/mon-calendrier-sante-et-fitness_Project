package com.example.mon_calendrier_sante_et_fitness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mon_calendrier_sante_et_fitness.data.repository.PeseeRepository
import com.example.mon_calendrier_sante_et_fitness.data.repository.SeanceRepository
import com.example.mon_calendrier_sante_et_fitness.data.repository.TypeExerciceRepository

class MainActivity : AppCompatActivity() {

    lateinit var seanceRepo: SeanceRepository
    lateinit var peseeRepo: PeseeRepository
    lateinit var typeRepo: TypeExerciceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seanceRepo = SeanceRepository(this)
        peseeRepo = PeseeRepository(this)
        typeRepo = TypeExerciceRepository(this)
    }
}