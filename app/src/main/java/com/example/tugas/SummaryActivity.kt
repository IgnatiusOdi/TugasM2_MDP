package com.example.tugas

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class SummaryActivity : AppCompatActivity() {
    lateinit var tvLocation: TextView
    lateinit var tvHarvest: TextView
    lateinit var btHarvest: Button
    lateinit var btRemove: Button
    lateinit var btBack: Button

    var wheat: Int = 0
    var plant: Farm = Farm("")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        tvLocation = findViewById(R.id.tvLocationSummary)
        tvHarvest = findViewById(R.id.tvHarvestSummary)
        btHarvest = findViewById(R.id.btHarvest)
        btRemove = findViewById(R.id.btRemove)
        btBack = findViewById(R.id.btBack)

        plant = intent.getParcelableExtra("plant")!!

        tvLocation.text = "Location : ${plant.location}"
        tvHarvest.text =
            "Harvest Countdown : \n"+
            "${plant.tillHarvestTime} Days"

        if (plant.tillHarvestTime == 0) {
            btHarvest.isEnabled = true
            btHarvest.setBackgroundColor(resources.getColor(R.color.purple_500))
            btRemove.isEnabled = false
            btRemove.setBackgroundColor(resources.getColor(R.color.gray))
        } else {
            btHarvest.isEnabled = false
            btHarvest.setBackgroundColor(resources.getColor(R.color.gray))
            btRemove.isEnabled = true
            btRemove.setBackgroundColor(resources.getColor(R.color.red))
        }

        btHarvest.setOnClickListener {
            wheat = 3
            Toast.makeText(this@SummaryActivity, "Berhasil harvest!", Toast.LENGTH_SHORT).show()
            returnToFarming()
        }

        btRemove.setOnClickListener {
            wheat = 1
            Toast.makeText(this@SummaryActivity, "Berhasil remove!", Toast.LENGTH_SHORT).show()
            returnToFarming()
        }

        btBack.setOnClickListener {
            finish()
        }
    }

    private fun returnToFarming() {
        val location = plant.location
        plant = Farm(location)

        val intent = Intent()
        intent.putExtra("plant", plant)
        intent.putExtra("wheat", wheat)
        setResult(RESULT_OK, intent)
        finish()
    }
}