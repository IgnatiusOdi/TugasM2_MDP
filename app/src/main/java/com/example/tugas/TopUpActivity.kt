package com.example.tugas

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener

class TopUpActivity : AppCompatActivity() {
    lateinit var etAmount: EditText
    lateinit var tvGold: TextView
    lateinit var btTopUp: Button
    lateinit var btCancel: Button

    var amount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topup)

        etAmount = findViewById(R.id.etAmountTopUp)
        tvGold = findViewById(R.id.tvGoldTopUp)
        btTopUp = findViewById(R.id.btTopUp)
        btCancel = findViewById(R.id.btCancelTopUp)

        etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (etAmount.text.toString().isNotBlank() && etAmount.text.toString().isDigitsOnly()) {
                    amount = etAmount.text.toString().toInt()
                    tvGold.text = (amount / 3000).toString()
                }
            }
        })

        btTopUp.setOnClickListener {
            if (amount % 3000 == 0) {
                amount /= 3000
                Toast.makeText(this@TopUpActivity, "Berhasil Top Up sebesar ${amount}G", Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("gold", amount)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this@TopUpActivity, "Amount harus kelipatan 3000",Toast.LENGTH_SHORT).show()
            }
        }

        btCancel.setOnClickListener {
            finish()
        }
    }
}