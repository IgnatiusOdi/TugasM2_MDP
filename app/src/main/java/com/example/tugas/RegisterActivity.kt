package com.example.tugas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirm: EditText
    lateinit var btRegister: Button
    lateinit var btToLogin: Button

    var users: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etNameRegister)
        etUsername = findViewById(R.id.etUsernameRegister)
        etPassword = findViewById(R.id.etPasswordRegister)
        etConfirm = findViewById(R.id.etConfirmRegister)
        btRegister = findViewById(R.id.btRegister)
        btToLogin = findViewById(R.id.btToLogin)
        users = intent.getParcelableArrayListExtra("users")!!

        btRegister.setOnClickListener {
            if (registerCheck()) {
                var user: User = User(etName.text.toString(), etUsername.text.toString(), etPassword.text.toString())
                users.add(user)
                Toast.makeText(this@RegisterActivity, "Berhasil register", Toast.LENGTH_SHORT).show()
                etName.setText("")
                etUsername.setText("")
                etPassword.setText("")
                etConfirm.setText("")
            }
        }

        btToLogin.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra("users", users)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun registerCheck(): Boolean {
        if (etName.text.isBlank() || etUsername.text.isBlank() || etPassword.text.isBlank() || etConfirm.text.isBlank()) {
            // FIELD KOSONG
            Toast.makeText(this@RegisterActivity, "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return false
        } else if (!etPassword.text.toString().equals(etConfirm.text.toString())) {
            // PASSWORD != KONFIRMASI
            Toast.makeText(this@RegisterActivity, "Password dan konfirmasi tidak sama!", Toast.LENGTH_SHORT).show()
            return false
        } else {
            // USERNAME TIDAK KEMBAR
            for (user in users) {
                if (user.username.equals(etUsername.text.toString())) {
                    Toast.makeText(this@RegisterActivity, "Username tidak boleh kembar!", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }
        return true
    }
}