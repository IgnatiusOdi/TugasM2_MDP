package com.example.tugas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly


class ShopActivity : AppCompatActivity() {
    lateinit var radioGroup: RadioGroup
    lateinit var rbBuy: RadioButton
    lateinit var rbSell: RadioButton
    lateinit var cbWheat: CheckBox
    lateinit var etAmount: EditText
    lateinit var tvCost: TextView
    lateinit var tvGold: TextView
    lateinit var btBuy: Button
    lateinit var btCancel: Button

    var mode = 2
    var price = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        radioGroup = findViewById(R.id.radioGroup)
        rbBuy = findViewById(R.id.rbBuy)
        rbSell = findViewById(R.id.rbSell)
        cbWheat = findViewById(R.id.cbWheat)
        etAmount = findViewById(R.id.etAmountShop)
        tvCost = findViewById(R.id.tvCostShop)
        tvGold = findViewById(R.id.tvGoldShop)
        btBuy = findViewById(R.id.btBuyShop)
        btCancel = findViewById(R.id.btCancelShop)

        var user: User = intent.getParcelableExtra("user")!!

        rbBuy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mode = 2
                btBuy.text = "BUY"
            } else {
                mode = 1
                btBuy.text = "SELL"
            }
            tvCost.text = "Cost : ${mode}g"
            priceChange()
        }

        cbWheat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etAmount.isEnabled = true
            } else {
                etAmount.isEnabled = false
                etAmount.setText("")
                priceChange()
            }
        }

        etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                priceChange()
            }
        })

        btBuy.setOnClickListener {
            if (etAmount.text.toString().isNotBlank() && etAmount.text.toString().isDigitsOnly()) {
                //BUY
                if (mode == 2) {
                    if (user.gold >= price) {
                        user.gold -= price
                        user.wheat += etAmount.text.toString().toInt()
                        Toast.makeText(this@ShopActivity, "Pembelian berhasil!", Toast.LENGTH_SHORT).show()
                        backToFarming(user)
                    } else {
                        Toast.makeText(this@ShopActivity, "Gold tidak mencukupi!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (user.wheat >= price) {
                        user.wheat -= price
                        user.gold += price
                        Toast.makeText(this@ShopActivity, "Penjualan berhasil!", Toast.LENGTH_SHORT).show()
                        backToFarming(user)
                    } else {
                        Toast.makeText(this@ShopActivity, "Wheat tidak mencukupi!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@ShopActivity, "Amount harus angka!", Toast.LENGTH_SHORT).show()
            }
        }

        btCancel.setOnClickListener {
            backToFarming(user)
        }
    }

    private fun backToFarming(user: User) {
        val resultIntent = Intent()
        resultIntent.putExtra("user", user)
        setResult(RESULT_OK, resultIntent)
        finish()
    }


    @SuppressLint("SetTextI18n")
    private fun priceChange() {
        price = if (etAmount.text.toString().isNotBlank() && etAmount.text.toString().isDigitsOnly()) {
            etAmount.text.toString().toInt() * mode
        } else {
            0
        }
        tvGold.text = "${price}G"
    }
}