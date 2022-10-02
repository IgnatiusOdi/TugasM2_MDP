package com.example.tugas

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class FarmingActivity : AppCompatActivity() {
    lateinit var tvName: TextView
    lateinit var tvGold: TextView
    lateinit var tvDay: TextView
    lateinit var tvWheat: TextView
    lateinit var btLogout: Button
    lateinit var btTopUp: Button
    lateinit var btShop: Button
    lateinit var btNextDay: Button
    lateinit var btShare: Button

    var user: User = User("", "", "")
    var farm: MutableList<MutableList<Button>> = mutableListOf()
    var indexSummary: ArrayList<Int> = arrayListOf()

    @SuppressLint("SetTextI18n", "IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farming)

        tvName = findViewById(R.id.tvName)
        tvGold = findViewById(R.id.tvGoldFarming)
        tvDay = findViewById(R.id.tvDayFarming)
        tvWheat = findViewById(R.id.tvWheat)
        btLogout = findViewById(R.id.btLogout)
        btTopUp = findViewById(R.id.btTopUpFarming)
        btShop = findViewById(R.id.btShopFarming)
        btNextDay = findViewById(R.id.btNextDay)
        btShare = findViewById(R.id.btShare)

        farm.add(mutableListOf(findViewById(R.id.tile00), findViewById(R.id.tile01), findViewById(R.id.tile02), findViewById(R.id.tile03)))
        farm.add(mutableListOf(findViewById(R.id.tile10), findViewById(R.id.tile11), findViewById(R.id.tile12), findViewById(R.id.tile13)))
        farm.add(mutableListOf(findViewById(R.id.tile20), findViewById(R.id.tile21), findViewById(R.id.tile22), findViewById(R.id.tile23)))
        farm.add(mutableListOf(findViewById(R.id.tile30), findViewById(R.id.tile31), findViewById(R.id.tile32), findViewById(R.id.tile33)))

        user = intent.getParcelableExtra("user")!!

        tvName.text = "Hi, ${user.name} !"
        tvGold.text = "G : ${user.gold}"
        tvDay.text = "Day : ${user.day}"
        tvWheat.text = "Wheat : ${user.wheat}"

        val goToSummary = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    user.farm[indexSummary[0]][indexSummary[1]] = data.getParcelableExtra("plant")!!
                    indexSummary = arrayListOf()
                    user.wheat += data.getIntExtra("wheat", 0)
                    tvWheat.text = "Wheat : ${user.wheat}"
                    refreshFarm()
                }
            }
        }

        for (i in 0 until 4) {
            for (j in 0 until 4) {
                refreshFarm()
                farm[i][j].setOnClickListener {
                    if (farm[i][j].text.toString() == "") {
                        if (user.wheat > 0) {
                            user.wheat -= 1
                            user.farm[i][j].planted = true

                            farm[i][j].text = " "
                            farm[i][j].setBackgroundColor(resources.getColor(R.color.yellow))
                            tvWheat.text = "Wheat : ${user.wheat}"
                            Toast.makeText(this@FarmingActivity, "Berhasil menanam!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@FarmingActivity, "Tidak punya wheat!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        indexSummary.add(i)
                        indexSummary.add(j)
                        val intent = Intent(this, SummaryActivity::class.java)
                        intent.putExtra("plant", user.farm[i][j])
                        goToSummary.launch(intent)
                    }
                }
                farm[i][j].setOnLongClickListener {
                    if (farm[i][j].text.toString() == "+") {
                        user.farm[i][j].expiredTime = 0
                        farm[i][j].text = " "
                        Toast.makeText(this@FarmingActivity, "Berhasil menyiram!", Toast.LENGTH_SHORT).show()
                        return@setOnLongClickListener true
                    }
                    return@setOnLongClickListener false
                }
            }
        }

        btLogout.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("user", user)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        val goToTopUp = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val gold = data.getIntExtra("gold", 0)
                    user.gold += gold
                    tvGold.text = "G : ${user.gold}"
                }
            }
        }

        btTopUp.setOnClickListener {
            val intent = Intent(this, TopUpActivity::class.java)
            goToTopUp.launch(intent)
        }

        val goToShop = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    user = data.getParcelableExtra("user")!!
                    tvGold.text = "G : ${user.gold}"
                    tvWheat.text = "Wheat : ${user.wheat}"
                }
            }
        }

        btShop.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            intent.putExtra("user", user)
            goToShop.launch(intent)
        }

        btNextDay.setOnClickListener {
            nextDay()
        }

        btShare.setOnClickListener {
            val email = Intent(Intent.ACTION_SENDTO)
            email.data = Uri.parse("mailto:")
            email.putExtra(Intent.EXTRA_SUBJECT, "Check My Farm")
            email.putExtra(Intent.EXTRA_TEXT,
                "Gold : ${user.gold}\n" +
                "Total playing time : ${user.day}\n" +
                "Total wheat : ${user.wheat}\n"
            )
            startActivity(Intent.createChooser(email, "Sending the email ..."))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun nextDay() {
        user.day += 1
        tvDay.text = "Day : ${user.day}"

        for (i in 0 until 4) {
            for (j in 0 until 4) {
                var plant = user.farm[i][j]
                if (plant.tillHarvestTime > 0) {
                    if (plant.planted) {
                        plant.expiredTime++
                        farm[i][j].text = "+"
                        when (plant.expiredTime) {
                            3 -> {
                                val location = plant.location
                                plant = Farm(location)
                                user.farm[i][j] = plant
                                farm[i][j].text = ""
                                farm[i][j].setBackgroundColor(resources.getColor(R.color.gray))
                                Toast.makeText(this@FarmingActivity, "Tanaman mati karena tidak pernah disiram!", Toast.LENGTH_SHORT).show()
                            }
                            1 -> {
                                plant.tillHarvestTime--
                                if (plant.tillHarvestTime == 0) farm[i][j].text = "*"
                            }
                            else -> {
                                plant.tillHarvestTime = plant.harvestTime
                            }
                        }
                    }
                }
            }
        }

        Toast.makeText(this@FarmingActivity, "Hari telah berganti!", Toast.LENGTH_SHORT).show()
    }

    private fun refreshFarm() {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val plant = user.farm[i][j]
                if (plant.planted) {
                    if (plant.tillHarvestTime == 0) {
                        farm[i][j].text = "*"
                    } else if (plant.expiredTime > 0) {
                        farm[i][j].text = "+"
                    } else {
                        farm[i][j].text = " "
                    }
                    farm[i][j].setBackgroundColor(resources.getColor(R.color.yellow))
                } else {
                    farm[i][j].text = ""
                    farm[i][j].setBackgroundColor(resources.getColor(R.color.gray))
                }
                farm[i][j].setTextColor(resources.getColor(R.color.black))
            }
        }
    }
}
